from __future__ import print_function
from importlib.metadata import metadata
import mimetypes
import re
from tkinter import E
from urllib import request
from googleapiclient.http import MediaFileUpload
from Google import Create_Service
from googleapiclient.discovery import build
import os
import io
from googleapiclient.http import MediaIoBaseDownload
from mp3 import cancion_dic
from apiclient import errors
from apiclient import http



CLIENT_SECRET_FILE = 'credentials.json'
API_NAME = 'drive'
API_VERSION = 'v3'
# SCOPES = ['https://www.googleapis.com/auth/drive.metadata']
SCOPES = ['https://www.googleapis.com/drive/v2/files/fileId']

service = Create_Service(CLIENT_SECRET_FILE, API_NAME, API_VERSION, SCOPES)


folder = service.files().list(q="mimeType='application/vnd.google-apps.folder' and name='FCmusic'",
                                spaces='drive',
                                fields='nextPageToken, files(id, name)'
                                ).execute()
print(folder)

#folder_id = '1S_sQJqpS0g5-ZNa-ZkWP5cgJJLD5Q7Mh'
folder_id = ((folder['files'])[0])['id']

path = '/home/demian/Documentos/Ingeniería de Software/Pruebas/canciones/'

query = f"parents = '{folder_id}'"

response = service.files().list(q=query).execute()
files = response.get('files')
nextPageToken = response.get('nextPageToken')

while nextPageToken:
    response = service.files().list(q=query).execute()
    files.extend(response.get('files'))
    nextPageToken = response.get('nextPageToken')

#print(files)

print()


for cancion in files:
    id = cancion['id']

    request = service.files().get_media(fileId=id)
    print(request.to_json())

    fh = io.BytesIO()
    downloader = MediaIoBaseDownload(fd=fh, request=request, chunksize=35896)

    done = False

    #while not done:
    status, done = downloader.next_chunk()
    #print('Download progress {0}'.format(status.progress()*100))

    fh.seek(0)
    # metadata = fh.read()
    # print(metadata.decode('ascii'))
    with open(path + cancion['name'], "wb") as f:
        f.write(fh.read())
        f.close()

arr = os.listdir('canciones/')

for cancion in arr:
    #imprime_cancion('canciones/' + cancion)
    print(cancion_dic('canciones/' + cancion))


def obten_canciones():
    canciones = []

    return canciones

def descarga_cancion(id):
    request = service.files().get_media(fileId=id)
    fh = io.BytesIO()
    downloader = MediaIoBaseDownload(fd=fh, request=request)
    done = False

    while not done:
        status, done = downloader.next_chunk()
        print('Download progress {0}'.format(status.progress()*100))

    fh.seek(0)
    with open(path + cancion['name'], "wb") as f:
        f.write(fh.read())
        f.close()