from django.db import models

class Artista(models.Model):
    artista_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)
    nombre_estilo = models.CharField(max_length = 30)


class Album(models.Model):
    album_id = models.IntegerField(primary_key = True)
    artistas = models.ManyToManyField(Artista)


class Genero(models.Model):
    genero_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)


class Cancion(models.Model):

    # cancion_id = models.IntegerField(primary_key = True)
    artista_id = models.ForeignKey(Artista, on_delete = models.CASCADE, null=True)
    album_id = models.ForeignKey(Album, on_delete = models.CASCADE, null=True)

    genero = models.ManyToManyField(Genero)

    drive_id = models.TextField(null=True)
    titulo = models.CharField(max_length = 30)
    titulo_estilo = models.CharField(max_length = 30)
    ruta = models.CharField(max_length = 50)
    año_lanzamiento = models.IntegerField(null=True)
    n_vistas = models.IntegerField(default=0)
    duracion = models.DurationField(null=True)

    def __str__(self):
        return self.titulo
    # class Meta:
    #     db_table = "Cancion"


class Usuario(models.Model):
    usuario_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)
    correo = models.EmailField()

    es_duenio = models.ManyToManyField(Cancion, related_name = "es_duenio")
    artista_seguido = models.ManyToManyField(Artista)
    me_gusta = models.ManyToManyField(Cancion, related_name = "me_gusta")
    estrellas = models.ManyToManyField(
        Cancion,
        related_name = "estrellas",
        through="Cancion_estrella")
    escuchadas_reciente = models.ManyToManyField(
        Cancion,
        related_name = "escuchadas_reciente",
        through="Escuchado_reciente")

    def __str__(self):
        return self.nombre


class Cancion_estrella(models.Model):
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    usuario_id = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    estrella = models.IntegerField()


class Escuchado_reciente(models.Model):
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    usuario_id = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    tiempo_escuchado = models.DateTimeField()


class Lista_reproduccion(models.Model):
    # lista_id = models.IntegerField(primary_key = True)
    usuario_id = models.IntegerField()
    estado_publico = models.BooleanField()
    nombre = models.CharField(max_length = 30)


class Cancion_lista_reproduccion(models.Model):
    lista_id = models.ForeignKey(Lista_reproduccion, on_delete=models.CASCADE)
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    indice = models.IntegerField()


class Notificacion(models.Model):
    notif_id = models.IntegerField(primary_key = True)
    usuario_id = models.OneToOneField(Usuario, on_delete=models.CASCADE)
    descripcion = models.TextField()
    tiempo_creado = models.DateTimeField()
    tiempo_visto = models.DateTimeField()
