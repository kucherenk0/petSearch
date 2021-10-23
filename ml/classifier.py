import sys
import os

# Saving the reference of the standard output
original_stdout = sys.stdout



searchId = sys.argv[1]
photoId = sys.argv[2]
photoPath = sys.argv[3]

resultFolderName = 'ml/result/search-' + searchId + '/'

if not os.path.exists(resultFolderName):
    os.mkdir(resultFolderName)

resultFileName =  resultFolderName + "pic-" + photoId + '.json'

jsonResult = """ "{
    "filePaths": [
    "file-storage/OUd_98776281_gettyimages-521697453.jpg",
    "file-storage/QmQ_98776281_gettyimages-521697453.jpg"
     ]
   }" """

with open(resultFileName, 'w+') as f:
    sys.stdout = f
    print(jsonResult)
    # Reset the standard output
    sys.stdout = original_stdout

print('This message will be written to the screen.')
