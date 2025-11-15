# Plan de Pruebas - Battleship P2P

## 📋 Resumen Ejecutivo
Este documento detalla el plan de pruebas unitarias para la aplicación Battleship P2P, cubriendo todas las clases principales del proyecto.

---

## 1️⃣ ProtocoloBattleshipTest

### Propósito
Validar la construcción y análisis de mensajes del protocolo de comunicación.

### Casos de Prueba

#### `testConstruirMensajeDisparo()`
- **Descripción**: Verifica que se construye correctamente un mensaje de disparo
- **Entrada**: Coordenadas (3, 5) y (0, 9)
- **Esperado**: `"DISPARAR|3,5"` y `"DISPARAR|0,9"`
- **Cobertura**: Formato correcto con separadores

#### `testConstruirMensajeResultado()`
- **Descripción**: Verifica construcción de mensajes de resultado
- **Entrada**: Resultado, coordenadas, y tipo de barco
- **Esperado**: `"IMPACTO|2,7|PORTAAVIONES"` y `"FALLO|1,1"`
- **Cobertura**: Con y sin tipo de barco

#### `testParsearMensajeDisparo()`
- **Descripción**: Parsea mensaje DISPARAR
- **Entrada**: `"DISPARAR|4,6"`
- **Esperado**: Objeto Mensaje con comando=DISPARAR, x=4, y=6
- **Cobertura**: Parsing básico de coordenadas

#### `testParsearMensajeImpacto()`
- **Descripción**: Parsea mensaje IMPACTO con tipo de barco
- **Entrada**: `"IMPACTO|3,5|CRUCERO"`
- **Esperado**: Comando IMPACTO, coordenadas (3,5), tipo CRUCERO

#### `testParsearMensajeFallo()`
- **Descripción**: Parsea mensaje FALLO sin tipo de barco
- **Entrada**: `"FALLO|2,8"`
- **Esperado**: Comando FALLO, coordenadas (2,8), sin tipo

#### `testParsearMensajeHundido()`
- **Descripción**: Parsea mensaje HUNDIDO
- **Entrada**: `"HUNDIDO|1,9|ACORAZADO"`
- **Esperado**: Comando HUNDIDO con tipo ACORAZADO

#### `testParsearMensajeComandoSimple()`
- **Descripción**: Parsea comando sin parámetros
- **Entrada**: `"LISTO"`
- **Esperado**: Comando LISTO, sin coordenadas ni tipo

#### `testParsearMensajeNulo()`
- **Descripción**: Manejo de mensaje nulo
- **Entrada**: `null`
- **Esperado**: Lanza `IllegalArgumentException`
- **Cobertura**: Validación defensiva

#### `testParsearMensajeVacio()`
- **Descripción**: Manejo de mensaje vacío
- **Entrada**: `""`
- **Esperado**: Lanza `IllegalArgumentException`

#### `testParsearMensajeFormatoInvalido()`
- **Descripción**: Manejo de formato con números inválidos
- **Entrada**: `"DISPARAR|abc,def"`
- **Esperado**: Lanza `IllegalArgumentException`

---

## 2️⃣ JuegoBattleshipTest

### Propósito
Validar la lógica del juego, colocación de barcos, disparos y estados.

### Casos de Prueba

#### `testColocarBarcosAutomaticamente()`
- **Descripción**: Verifica que todos los barcos se colocan correctamente
- **Entrada**: Invocación sin parámetros
- **Esperado**: 17 casillas ocupadas (5+4+3+3+2)
- **Cobertura**: Lógica de colocación aleatoria

#### `testEstaBarcoHundido()`
- **Descripción**: Verifica la lógica de hundimiento
- **Entrada**: Tipo de barco, número de impactos incrementado
- **Esperado**: `false` inicialmente, `true` cuando impactos ≥ tamaño
- **Cobertura**: Estados de barcos

#### `testGetBarcos()`
- **Descripción**: Verifica que existen todos los barcos con sus tamaños
- **Entrada**: Invocación sin parámetros
- **Esperado**: 5 barcos con tamaños correctos (5,4,3,3,2)
- **Cobertura**: Inicialización correcta

#### `testGetImpactosPorBarco()`
- **Descripción**: Verifica contadores de impactos iniciales
- **Entrada**: Invocación sin parámetros
- **Esperado**: Todos los barcos con 0 impactos
- **Cobertura**: Estado inicial

#### `testGetTableroEnemigo()`
- **Descripción**: Verifica estado inicial del tablero enemigo
- **Entrada**: Invocación sin parámetros
- **Esperado**: Matriz 10x10 llena de '?'
- **Cobertura**: Inicialización

#### `testGetTableroPropio()`
- **Descripción**: Verifica estado inicial del tablero propio
- **Entrada**: Invocación sin parámetros
- **Esperado**: Matriz 10x10 llena de '~' (agua)
- **Cobertura**: Inicialización

#### `testObtenerTipoBarcoEn()`
- **Descripción**: Obtiene tipo de barco en posición
- **Entrada**: Coordenadas después de colocar barcos
- **Esperado**: Tipo válido de barco o "DESCONOCIDO"
- **Cobertura**: Mapeo de caracteres

#### `testRecibirDisparo()`
- **Descripción**: Procesa disparo recibido
- **Entrada**: Coordenadas (0,0)
- **Esperado**: `false` en agua, `true` en barco, 'O' en fallo
- **Cobertura**: Lógica de impacto/fallo

#### `testRegistrarFallo()`
- **Descripción**: Registra fallo en tablero enemigo
- **Entrada**: Coordenadas (2,3)
- **Esperado**: 'O' en tablero, posición marcada como disparada
- **Cobertura**: Registro de disparos

#### `testRegistrarImpacto()`
- **Descripción**: Registra impacto en tablero enemigo
- **Entrada**: Coordenadas (5,5)
- **Esperado**: 'X' en tablero, posición marcada como disparada
- **Cobertura**: Registro de impactos

#### `testTodosBarcosHundidos()`
- **Descripción**: Verifica condición de victoria
- **Entrada**: Estado del juego
- **Esperado**: `false` inicialmente
- **Cobertura**: Condición de fin de juego

#### `testYaDisparado()`
- **Descripción**: Verifica si posición ya fue disparada
- **Entrada**: Coordenadas (3,4) sin disparar, luego (3,4) con disparo
- **Esperado**: `false`, luego `true`
- **Cobertura**: Validación de disparos repetidos

---

## 3️⃣ VistaConsolaTest

### Propósito
Validar la interacción con el usuario (input/output).

### Casos de Prueba

#### `testCerrar()`
- **Descripción**: Cierre limpio del scanner
- **Entrada**: Sin parámetros
- **Esperado**: No lanza excepciones
- **Cobertura**: Liberación de recursos

#### `testElegirModo()`
- **Descripción**: Selección de modo servidor (opción 1)
- **Entrada**: `"1\n"`
- **Esperado**: Devuelve 1
- **Cobertura**: Validación de entrada

#### `testElegirModoOpcion2()`
- **Descripción**: Selección de modo cliente (opción 2)
- **Entrada**: `"2\n"`
- **Esperado**: Devuelve 2
- **Cobertura**: Alternativa válida

#### `testMostrarBienvenida()`
- **Descripción**: Muestra mensaje de bienvenida
- **Entrada**: Sin parámetros
- **Esperado**: Output contiene "BATTLESHIP"
- **Cobertura**: Output correcto

#### `testMostrarError()`
- **Descripción**: Muestra mensaje de error
- **Entrada**: `"Error de prueba"`
- **Esperado**: Output contiene el mensaje
- **Cobertura**: Error handling

#### `testMostrarEstadoBarcos()`
- **Descripción**: Muestra estado de todos los barcos
- **Entrada**: Mapas de barcos e impactos
- **Esperado**: Output con nombres de barcos
- **Cobertura**: Formateo de información

#### `testMostrarMensaje()`
- **Descripción**: Muestra mensaje general
- **Entrada**: `"Mensaje de prueba"`
- **Esperado**: Output contiene el mensaje
- **Cobertura**: Output estándar

#### `testMostrarTablero()`
- **Descripción**: Muestra tablero 10x10
- **Entrada**: Matriz 10x10, título
- **Esperado**: Output con título y símbolos
- **Cobertura**: Formateo de tablero

#### `testObtenerDisparo()`
- **Descripción**: Obtiene coordenadas de disparo válidas
- **Entrada**: `"3,5\n"`
- **Esperado**: Array [3, 5]
- **Cobertura**: Parsing de coordenadas

#### `testObtenerDisparoCoordenadasFuera()`
- **Descripción**: Rechaza coordenadas fuera de rango
- **Entrada**: `"15,20\n2,2\n"`
- **Esperado**: Array [2, 2] (reintento)
- **Cobertura**: Validación de rango

#### `testObtenerDisparoYaDisparado()`
- **Descripción**: Rechaza posición ya disparada
- **Entrada**: `"1,1\n4,4\n"` (1,1 ya disparado)
- **Esperado**: Array [4, 4]
- **Cobertura**: Prevención de disparos duplicados

#### `testObtenerIPServidor()`
- **Descripción**: Obtiene IP de servidor
- **Entrada**: `"192.168.1.100\n"`
- **Esperado**: `"192.168.1.100"`
- **Cobertura**: Input de IP

#### `testObtenerNombreJugador()`
- **Descripción**: Obtiene nombre del jugador
- **Entrada**: `"JugadorTest\n"`
- **Esperado**: `"JugadorTest"`
- **Cobertura**: Input de nombre

#### `testPreguntarReintento()`
- **Descripción**: Pregunta si reintentará (sí)
- **Entrada**: `"s\n"`
- **Esperado**: `true`
- **Cobertura**: Decisión del usuario

#### `testPreguntarReintentoNo()`
- **Descripción**: Pregunta si reintentará (no)
- **Entrada**: `"n\n"`
- **Esperado**: `false`
- **Cobertura**: Opción negativa

---

## 4️⃣ ConexionP2PTest

### Propósito
Validar la comunicación P2P entre jugadores.

### Casos de Prueba

#### `testCerrar()`
- **Descripción**: Cierre seguro de conexión
- **Entrada**: Conexión no inicializada
- **Esperado**: No lanza excepciones
- **Cobertura**: Manejo defensivo

#### `testConectar()`
- **Descripción**: Conexión cliente-servidor
- **Entrada**: IP "localhost", puerto 12346
- **Esperado**: Conexión exitosa sin excepciones
- **Cobertura**: Comunicación TCP

#### `testEnviarMensaje()`
- **Descripción**: Envío de mensaje entre jugadores
- **Entrada**: `"Hola desde cliente"`
- **Esperado**: Servidor recibe mensaje idéntico
- **Cobertura**: Transmisión de datos

#### `testEsperarConexion()`
- **Descripción**: Servidor espera conexión
- **Entrada**: Puerto 12348
- **Esperado**: Acepta conexión de cliente
- **Cobertura**: Modo servidor

#### `testGetDireccionRemota()`
- **Descripción**: Obtiene dirección del peer remoto
- **Entrada**: Después de conectar
- **Esperado**: String no vacío con dirección
- **Cobertura**: Información de conexión

#### `testLeerMensaje()`
- **Descripción**: Recepción de mensaje
- **Entrada**: Servidor envía `"Mensaje del servidor"`
- **Esperado**: Cliente recibe mensaje idéntico
- **Cobertura**: Lectura de datos

---

## 5️⃣ BattleshipControladorTest

### Propósito
Validar el flujo principal del juego (controlador).

### Casos de Prueba

#### `testIniciar()`
- **Descripción**: Inicialización del juego
- **Entrada**: Invocación sin parámetros
- **Esperado**: Controlador se crea sin excepciones
- **Cobertura**: Creación de objetos
- **Limitación**: Requiere entrada de usuario (no simulable sin mocking)

#### `testMain()`
- **Descripción**: Punto de entrada principal
- **Entrada**: Invocación desde main
- **Esperado**: No lanza excepciones
- **Cobertura**: Documentación
- **Limitación**: Requiere interacción completa del usuario

---

## 📊 Resumen de Cobertura

| Clase | Total Tests | Tipos de Pruebas |
|-------|-----------|------------------|
| ProtocoloBattleship | 10 | Construcción, parsing, validación |
| JuegoBattleship | 12 | Lógica de juego, estados, getters |
| VistaConsola | 15 | Input/output, validación |
| ConexionP2P | 6 | Comunicación TCP, mensajes |
| BattleshipControlador | 2 | Flujo principal |
| **TOTAL** | **45** | **Unitarias e integración** |

---

## ⚠️ Consideraciones Importantes

1. **Tests de ConexionP2P**: Utilizan threading para simular servidor y cliente
2. **Tests de VistaConsola**: Redirigen System.in/out para simular entrada/salida
3. **Tests de BattleshipControlador**: Limitados por naturaleza interactiva (candidatos para mock)
4. **Aleatoriedad**: `testColocarBarcosAutomaticamente()` maneja colocación aleatoria

---

## 🚀 Ejecución de Pruebas

```bash
# Ejecutar todas las pruebas
gradle test

# Ejecutar pruebas de una clase específica
gradle test --tests ProtocoloBattleshipTest

# Con reporte de cobertura
gradle test jacocoTestReport
```

---

## 📝 Próximos Pasos

1. Implementar mocking para `BattleshipControlador` (Mockito)
2. Agregar pruebas de integración (flujo completo cliente-servidor)
3. Pruebas de estrés para comunicación P2P
4. Validar cobertura de líneas > 80%
