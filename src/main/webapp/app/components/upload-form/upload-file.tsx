import './upload-file.scss';

import React, { useEffect, useRef, useState } from 'react';

export const UploadFile = () => {
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [isFilePicked, setIsFilePicked] = useState(false);
  const fileInput = useRef(null);

  function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      // тут формирую формат файла, как видишь рабочий урл для превью надо отдельно делать с помощью reader
      reader.onload = () =>
        resolve({
          url: reader.result,
          name: file?.name,
          size: file?.size,
        });
      reader.onerror = error => reject(error);
    });
  }

  const changeHandler = async event => {
    // получаем файлы из инпута и конструируем url
    const results = await Promise.all(
      Array.from(event.target.files).map(file => {
        return Promise.resolve(
          // это чтобы Promise.all не валился) на ошибках
          getBase64(file)
            // тут уже сконструированный объект помещаю либо говорю мол ошибка
            .then(res => ({
              type: 'success',
              file: res,
            }))
            .catch(err => ({
              type: 'error',
              msg: err,
            }))
        );
      })
    );
    setSelectedFiles(results);
  };

  const handleSubmission = () => {
    const formData = new FormData();
    // поправь плз)
    // formData.append('File', selectedFiles);

    fetch('https://freeimage.host/api/1/upload?key=<YOUR_API_KEY>', {
      method: 'POST',
      body: formData,
    })
      .then(response => response.json())
      .then(result => {
        // console.log('Success:', result);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  };

  return (
    <div>
      <input type="file" name="file" ref={fileInput} multiple onChange={changeHandler} />
      {/* тут я массив полученных урлов привязываю к img*/}
      {selectedFiles.length ? (
        selectedFiles.map(fileObj =>
          fileObj.type === 'success' ? (
            <div key={fileObj.file.url}>
              <img src={fileObj.file.url} />
              <p>Filename: {fileObj.file.name}</p>
              <p>Size in bytes: {fileObj.file.size}</p>
            </div>
          ) : (
            <p> Случился Error: {fileObj.msg}</p>
          )
        )
      ) : (
        <p>Select a file to show details</p>
      )}
      <div>
        <button onClick={handleSubmission}>Submit</button>
      </div>
    </div>
  );
};

export default UploadFile;
