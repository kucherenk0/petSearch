import sys
import os

# Saving the reference of the standard output
original_stdout = sys.stdout

searchId = sys.argv[1]
photoId = sys.argv[2]
photoPath = sys.argv[3]

resultFolderName = 'ml/results/search-' + searchId

print("exists: " + str(os.path.isdir(resultFolderName)))
if not os.path.isdir(resultFolderName):
    os.mkdir(resultFolderName)

resultFileName =  resultFolderName + "/pic-" + photoId + '.json'

jsonResult = """ [
                    {
                      "filePath": "file-storage/OUd_98776281_gettyimages-521697453.jpg",
                       "dateOfShoot": "2020-01-01",
                       "photoAddress": "Москва, Васильевская 4"
                    }
                  ]
"""

with open(resultFileName, 'w+') as f:
    sys.stdout = f
    print(jsonResult)
    # Reset the standard output
    sys.stdout = original_stdout

print('This message will be written to the screen.')
