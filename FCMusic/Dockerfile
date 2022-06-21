# Tutorial de https://medium.com/backticks-tildes/how-to-dockerize-a-django-application-a42df0cb0a99

FROM python:3.9

# Python output to terminal without bufferint first
ENV PYTHONUNBUFFERED=1

RUN mkdir /FCMusic
RUN git clone https://github.com/ErikdelRio/FCMusic.git /app

WORKDIR /app/FCMusic

RUN apt -y update
RUN apt-get -y install libgmp-dev libmpfr-dev libmpc-dev

RUN python -m pip install -U pip
RUN python -m pip install -r requirements.txt

EXPOSE 8000
