# Tutorial de https://medium.com/backticks-tildes/how-to-dockerize-a-django-application-a42df0cb0a99

FROM python:3.10

# Python output to terminal without bufferint first
ENV PYTHONUNBUFFERED=1

RUN mkdir /FCMusic

WORKDIR /FCMusic

ADD ./FCMusic /FCMusic

RUN pip install -r ./FCMusic/requirements.txt

EXPOSE 8000
