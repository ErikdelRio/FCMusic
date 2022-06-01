# Generated by Django 4.0.4 on 2022-05-31 19:03

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Album',
            fields=[
                ('album_id', models.IntegerField(primary_key=True, serialize=False)),
            ],
        ),
        migrations.CreateModel(
            name='Artista',
            fields=[
                ('artista_id', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre', models.CharField(max_length=30)),
                ('nombre_estilo', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='Cancion',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('titulo', models.CharField(max_length=30)),
                ('titulo_estilo', models.CharField(max_length=30)),
                ('ruta', models.CharField(max_length=50)),
                ('año_lanzamiento', models.IntegerField(null=True)),
                ('n_vistas', models.IntegerField(default=0)),
                ('duracion', models.DurationField(null=True)),
                ('album_id', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='sitio.album')),
                ('artista_id', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='sitio.artista')),
            ],
        ),
        migrations.CreateModel(
            name='Cancion_estrella',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('estrella', models.IntegerField()),
                ('cancion_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.cancion')),
            ],
        ),
        migrations.CreateModel(
            name='Escuchado_reciente',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('tiempo_escuchado', models.DateTimeField()),
                ('cancion_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.cancion')),
            ],
        ),
        migrations.CreateModel(
            name='Genero',
            fields=[
                ('genero_id', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='Lista_reproduccion',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('usuario_id', models.IntegerField()),
                ('estado_publico', models.BooleanField()),
                ('nombre', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='Usuario',
            fields=[
                ('usuario_id', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre', models.CharField(max_length=30)),
                ('correo', models.EmailField(max_length=254)),
                ('artista_seguido', models.ManyToManyField(to='sitio.artista')),
                ('es_duenio', models.ManyToManyField(related_name='es_duenio', to='sitio.cancion')),
                ('escuchadas_reciente', models.ManyToManyField(related_name='escuchadas_reciente', through='sitio.Escuchado_reciente', to='sitio.cancion')),
                ('estrellas', models.ManyToManyField(related_name='estrellas', through='sitio.Cancion_estrella', to='sitio.cancion')),
                ('me_gusta', models.ManyToManyField(related_name='me_gusta', to='sitio.cancion')),
            ],
        ),
        migrations.CreateModel(
            name='Notificacion',
            fields=[
                ('notif_id', models.IntegerField(primary_key=True, serialize=False)),
                ('descripcion', models.TextField()),
                ('tiempo_creado', models.DateTimeField()),
                ('tiempo_visto', models.DateTimeField()),
                ('usuario_id', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, to='sitio.usuario')),
            ],
        ),
        migrations.AddField(
            model_name='escuchado_reciente',
            name='usuario_id',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.usuario'),
        ),
        migrations.CreateModel(
            name='Cancion_lista_reproduccion',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('indice', models.IntegerField()),
                ('cancion_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.cancion')),
                ('lista_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.lista_reproduccion')),
            ],
        ),
        migrations.AddField(
            model_name='cancion_estrella',
            name='usuario_id',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sitio.usuario'),
        ),
        migrations.AddField(
            model_name='cancion',
            name='genero',
            field=models.ManyToManyField(to='sitio.genero'),
        ),
        migrations.AddField(
            model_name='album',
            name='artistas',
            field=models.ManyToManyField(to='sitio.artista'),
        ),
    ]