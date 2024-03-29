# Examen ML - Mutantes

## Introducción
Este proyecto Java-Springboot implementa una solución para el ejercicio [Mutantes](https://github.com/dmatsudo/ml-exercise/blob/master/src/main/resources/doc/Examen%20Mercadolibre%20%20-%20Mutantes.pdf) utilizando Google App Engine para su despliegue.

## Supuestos
A continuación se detallan definiciones que se asumieron a partir del enunciado del ejercicio:
1. Cada posición (letra) del ADN puede ser reutilizada en diferentes secuencias. Se asume que dos secuencias son diferentes si tienen al menos una posición que no coincida.
2. Las secuencias para identificar mutantes son de cuatro letras iguales y pueden ser horizontal (-), vertical (|), oblicua hacia la derecha (\\) u oblicua hacia la izquierda (/).
3. Validaciones (algunas ya definidas en el enunciado):
   - Los ADNs son representados por una matriz cuadrada (NxN)
   - Las letras de un ADN sólo pueden ser sólo mayúsculas (A, T, C, G)
   - Un ADN no puede ser vacío.
   
   En caso de un request con un ADN que no cumpla con las validaciones se retorna un HTTP 400 Bad Request

## Descripción técnica
La aplicación se desarrolló en una arquitectura de tres capas (Controller, Service, DAO), usando Spring como inyector de dependencias.
En cuanto a la base de datos el modelo tiene dos tablas: *human_info* con sólo una columna DNA y *human_info_stats* que contiene la información requerida por el servicio stats.

La lógica para determinar si un ADN es mutante está implementada en el siguiente método de la clase HumanServiceImpl (se modificó la interface para retornar una checked exception para fallas de validación):
```
boolean processHumanInfo(HumanInfo humanInfo) throws InvalidDNAException
```
Para intentar optimizar los tiempos de ejecución del algoritmo se hace una única recorrida de la matriz para encontrar secuencias. 
En cada posición se evalúa el comienzo de secuencias en todas las direcciones posibles (horizontal, vertical, diagonales). La evaluación sólo se realiza si tiene sentido, por ejemplo: no se evalúa horizontalmente la posición (0, 3) en un ADN de 6x6 (una secuencia horizontal no puede tener una longitud mayor a 3 para esa posición).

Para cada dirección se decidió tener cuatro métodos por separado (`checkHorizontalSeq`, `checkVerticalSeq`, `checkDiagonalSeq`, `checkBackDiagonalSeq`) en lugar de refactorizar para tener un único método con la idea de tener un código más simple (aunque más extenso).

## Instrucciones de ejecución
Para ejectuar servicio **mutant** ejecutar una llamada POST al siguiente endpoint:
```
https://ml-exercise-287611.rj.r.appspot.com/ml-exercise/api/mutant
```
Parámetros:
- dna: array de Strings que representan cada fila de una tablade (NxN) con la secuencia del ADN.

Retorna:
- HTTP 200-OK: si el ADN corresponde a un mutante
- HTTP 403-Forbidden: si el ADN no corresponde a un mutante
- HTTP 400-Bad Request: si el ADN no es válido

Para ejecutar servicio **stats** realizar un request HTTP GET al siguente endpoint:
```
https://ml-exercise-287611.rj.r.appspot.com/ml-exercise/api/stats
```
Como referencia puede utilizarse la siguiente [Postman Collection](https://github.com/dmatsudo/ml-exercise/blob/master/src/main/resources/doc/ml-exercise%20AppEngine.postman_collection.json).

## Posibles mejoras
Para la persistencia se usó una base de datos MySQL. Teniendo en cuenta las fluctuaciones agresivas de tráfico tal vez sea mejor el uso de una base de datos NoSQL (Bigtable). Se dejó preparada la arquitectura para implementar otro DAO y se comenzó el análisis pero no se llegó a desarrollar.
