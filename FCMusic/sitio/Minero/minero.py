from __future__ import print_function
from importlib.metadata import metadata
import mimetypes
import re
from tkinter import E
from urllib import request
from googleapiclient.http import MediaFileUpload
from .Google import Create_Service
from googleapiclient.discovery import build
import os
import io
from googleapiclient.http import MediaIoBaseDownload
from .mp3 import cancion_dic
from apiclient import errors
from apiclient import http


def conecta_drive():
	CLIENT_SECRET_FILE = 'credentials.json'
	API_NAME = 'drive'
	API_VERSION = 'v3'
	SCOPES = ['https://www.googleapis.com/auth/drive']
	service = Create_Service(CLIENT_SECRET_FILE, API_NAME, API_VERSION, SCOPES)
	path = 'sitio/Minero/canciones/'
	c = []

	folder = service.files().list(q="mimeType='application/vnd.google-apps.folder' and name='FCmusic'",
		                            spaces='drive',
		                            fields='nextPageToken, files(id, name)'
		                            ).execute()

	folder_id = ((folder['files'])[0])['id']
	query = f"parents = '{folder_id}' and mimeType='audio/mpeg'"
	response = service.files().list(q=query).execute()
	files = response.get('files')
	nextPageToken = response.get('nextPageToken')

	while nextPageToken:
		response = service.files().list(q=query).execute()
		files.extend(response.get('files'))
		nextPageToken = response.get('nextPageToken')

	for cancion in files:
		if(cancion['mimeType'] != 'audio/mpeg'):
			continue

		id = cancion['id']
		cancion_ruta = path + cancion['name']
		request = service.files().get_media(fileId=id)
		fh = io.BytesIO()
		downloader = MediaIoBaseDownload(fd=fh, request=request, chunksize=35896)
		# Si lo quito da error{
		done = False
		status, done = downloader.next_chunk()
		# }
		fh.seek(0)
		with open(cancion_ruta, "wb") as f:
			f.write(fh.read())
			f.close()

		dict = cancion_dic(cancion_ruta)
		dict['drive_id'] = id
		c.append(dict)
		os.remove(cancion_ruta)

	return c	


def descarga_cancion(id):
	CLIENT_SECRET_FILE = 'credentials.json'
	API_NAME = 'drive'
	API_VERSION = 'v3'
	SCOPES = ['https://www.googleapis.com/auth/drive']
	service = Create_Service(CLIENT_SECRET_FILE, API_NAME, API_VERSION, SCOPES)
	path = 'sitio/Minero/canciones/'

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


if __name__ == "__main__":
	canciones = conecta_drive()
	for cancion in canciones:
		print(cancion)