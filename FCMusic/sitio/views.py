from django.shortcuts import render
from django.http import HttpResponse

from .models import Cancion
import json

# Create your views here.
def escuchar(request):
    return render(request, 'cancion/escuchar.html')

def getCancion(request):
    body_unicode = request.body
    body = json.loads(body_unicode)

    titulo_req = body['titulo']
    result = list(Cancion.objects.filter(titulo__icontains=titulo_req))

    canciones = {
        "canciones": result
    }
    return HttpResponse(json.dumps(canciones))
    # return render(request, json.dumps(canciones))
