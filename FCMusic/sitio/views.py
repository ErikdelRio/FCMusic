from django.shortcuts import render
from django.http import HttpResponse,HttpResponseNotFound, HttpResponseNotAllowed

from .models import Cancion,Lista_reproduccion,Cancion_lista_reproduccion
import json
from django.core import serializers

# Create your views here.
# Obtiene el body de la petición, y regresa un objeto a partir del json obtenido
def parseRequest(req):
    body_unicode = req.body
    body = json.loads(body_unicode)
    return body

# Obtiene las canciones de una lista a partir de una petición
# Regresa la lista de canciones que se obtuvieron
def getCancionesLista(request):
    body = parseRequest(request)

    user_id = request.user.id
    lista_id = body['lista_id']
    res = Cancion_lista_reproduccion.objects \
        .filter(Lista_reproduccion=lista_id) \
        .selectRelated('Cancion')

    return list(res)

def getListasBD(request):
    listas = Lista_reproduccion \
        .objects \
        .filter(usuario_id=request.user.id)

    json_res = serializers.serialize('json', list(listas))
    return json_res

# Obtiene las canciones a partir de su nombre
# Regresa una respuesta en json con la lista de las canciones encontradas
def getCancion(request):
    body = parseRequest(request)

    titulo_req = body['titulo']
    result = list(Cancion.objects.filter(titulo__icontains=titulo_req))

    json_res = serializers.serialize('json', result)
    return HttpResponse(json_res)

def getListas(request):
    listas = getListasBD(request)
    return HttpResponse(listas)


def home(request):
    listas = getListasBD(request)

    # json_res = serializers.serialize('json', listas)
    return render(request, 'home.html', {'listas': listas})

def escuchar(request):
    return render(request, 'cancion/escuchar.html')


def escucharLista(request):
    lista = getCancionesLista(request)

    return render(request, 'cancion/escuchar.html', {'lista': lista})

def creaLista(request):
    return render(request, 'cancion/creaLista.html')

def putLista(request):
    body = parseRequest(request)
    nuevaLista = Lista_reproduccion()
    nuevaLista.usuario_id = request.user.id
    nuevaLista.estado_publico = body['estado']
    nuevaLista.nombre = body['nombre']
    nuevaLista.save()

    i = 1
    for c in body['canciones']:
        nuevaCancionLista = Cancion_lista_reproduccion()
        nuevaCancionLista.lista_id = nuevaLista.id
        nuevaCancionLista.cancion_id = c.cancion_id
        nuevaCancionLista.indice = i
        nuevaCancionLista.save()
        i+=1

    return HttpResponseRedirect('/home')


# Errores

def error404(request, exception):
    return HttpResponseNotFound()
    # return render(request, 'no_encontrada.html')

def error400(request, exception):
    return render(request, 'errores/error400.html')

def error403(request, exception):
    return render(request, 'errores/error403.html')

def error500(request):
    return render(request, 'errores/error500.html')
