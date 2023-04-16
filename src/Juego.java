public class Juego {
    private static Preguntas preguntas = new Preguntas(16);
    private static Juego juego = new Juego();

    private int preguntaActual = 0;
    private boolean preguntaSaltada = false;
    private boolean cincuentaCincuenta = false;
    private int numeroPreguntaCincuentaCincuenta = -1;

    public static String jugar(String param) {

        String jsonResponse = "";
        debug();

        try {
            if (param == null || param.isEmpty()) {
                param = "inicio";
            }

            if (!param.matches("[0-5]") && !param.matches("inicio")) {
                jsonResponse = juego.obtenerMenu(param);
                return jsonResponse;
            }

            if (param.matches("inicio")) {
                juego.reiniciarJuego();
                jsonResponse = juego.obtenerMenu(param);
                return jsonResponse;
            }

            if (param.matches("4")) {
                juego.cincuentaCincuenta();
                jsonResponse = juego.obtenerMenu(param);
                return jsonResponse;
            }

            if (param.matches("5")) {
                juego.saltarPregunta();
                jsonResponse = juego.obtenerMenu(param);
                return jsonResponse;
            }

            if (juego.preguntaActual < preguntas.obtenerNumeroPreguntas()
                    && juego.obtenerIndexCorrecta() == Integer.parseInt(param)) {
                juego.siguientePregunta();
                jsonResponse = juego.obtenerMenu(param);
                return jsonResponse;
            }

            if (juego.obtenerIndexCorrecta() != Integer.parseInt(param)) {
                jsonResponse = juego.terminarJuego();
                juego.reiniciarJuego();
                return jsonResponse;
            }

            if (juego.preguntaActual == preguntas.obtenerNumeroPreguntas()
                    && juego.obtenerIndexCorrecta() == Integer.parseInt(param)) {
                jsonResponse = juego.ganarJuego();
                juego.reiniciarJuego();
                return jsonResponse;
            }

            jsonResponse = juego.obtenerMenu(param);

        } catch (NumberFormatException e) {
            System.err.println("Error: el parámetro debe ser un número");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error: el parámetro no puede ser nulo");
            e.printStackTrace();
        }

        catch (Exception e) {
            System.err.println("Error:");
            e.printStackTrace();
        }

        return jsonResponse;

    }

    public String obtenerMenu(String respuesta) {

        try {
            String respuestaJson = "totalPreguntas: " + (preguntas.obtenerNumeroPreguntas() + 1) + "\n";
            respuestaJson += "preguntaNumero: " + (preguntaActual + 1) + "\n";
            respuestaJson += "totalPuntos: " + (preguntaActual * 100) + "\n";
            respuestaJson += "pregunta: " + "\n" + mostrarPreguntas() + "\n";

            return respuestaJson;
        } catch (Exception e) {
            System.err.println("Error al obtener el menú:");
            e.printStackTrace();
            return "";
        }
    }

    public int obtenerIndexCorrecta() {
        try {
            return preguntas.obtenerPregunta(juego.preguntaActual).obtenerIndexRespuestaCorrecta();
        } catch (Exception e) {
            System.err.println("Error al obtener el índice de la respuesta correcta:");
            e.printStackTrace();
            return -1;
        }
    }

    public void siguientePregunta() {
        try {
            preguntaActual++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ganarJuego() {
        try {
            String respuestaJson = "estado: " + "ganaste" + "\n";
            respuestaJson += "preguntasCorrectas: " + (preguntaActual + 1) + "\n";
            respuestaJson += "puntos: " + ((preguntaActual + 1) * 100) + "\n";
            respuestaJson += "Escribe cualquier cosa para volver a jugar";

            return respuestaJson;
        } catch (Exception e) {
            System.err.println("Error al obtener el menú:");
            e.printStackTrace();
            return "";
        }
    }

    public String terminarJuego() {

        try {
            String respuestaJson = "estado: " + "perdiste" + "\n";
            respuestaJson += "preguntasCorrectas: " + preguntaActual + "\n";
            respuestaJson += "puntos: " + ((preguntaActual + 1) * 100) + "\n";
            respuestaJson += "Escribe cualquier cosa para volver a jugar";
            return respuestaJson;
        } catch (Exception e) {
            System.err.println("Error al obtener el menú:");
            e.printStackTrace();
            return "";
        }
    }

    public void reiniciarJuego() {
        preguntaActual = 0;
        cincuentaCincuenta = false;
        preguntaSaltada = false;
        preguntas.mezclarPreguntas();
    }

    public void saltarPregunta() {

        if (preguntaSaltada) {
            return;
        }

        if (numeroPreguntaCincuentaCincuenta == preguntaActual && cincuentaCincuenta) {
            numeroPreguntaCincuentaCincuenta = -1;
        }

        preguntas.cambiarPregunta(preguntaActual);

        preguntaSaltada = true;

    }

    public void cincuentaCincuenta() {
        if (cincuentaCincuenta) {
            return;
        }
        cincuentaCincuenta = true;
        numeroPreguntaCincuentaCincuenta = preguntaActual;
    }

    public static String mostrarPreguntas() {

        System.out.println("Pregunta actual: " + juego.preguntaActual);
        System.out.println("Preguntas totales: " + preguntas.obtenerNumeroPreguntas());

        String saltarCincuentaCincuenta = "4: " + "50/50";
        String saltarPregunta = "5: " + "Saltar pregunta";

        String pregunta = preguntas.obtenerPregunta(juego.preguntaActual).obtenerTextoPregunta();
        String respuesta1 = preguntas.obtenerPregunta(juego.preguntaActual).obtenerOpcionesRespuesta().get(0);
        String respuesta2 = preguntas.obtenerPregunta(juego.preguntaActual).obtenerOpcionesRespuesta().get(1);
        String respuesta3 = preguntas.obtenerPregunta(juego.preguntaActual).obtenerOpcionesRespuesta().get(2);
        String respuesta4 = preguntas.obtenerPregunta(juego.preguntaActual).obtenerOpcionesRespuesta().get(3);

        if (juego.preguntaSaltada) {
            saltarPregunta = "";
        }

        if (juego.cincuentaCincuenta) {
            saltarCincuentaCincuenta = "";
        }

        if (juego.cincuentaCincuenta && juego.numeroPreguntaCincuentaCincuenta == juego.preguntaActual) {
            int correcta = preguntas.obtenerPregunta(juego.preguntaActual).obtenerIndexRespuestaCorrecta();
            if (correcta == 0) {
                respuesta2 = "";
                respuesta3 = "";
            } else if (correcta == 1) {
                respuesta1 = "";
                respuesta4 = "";
            } else if (correcta == 2) {
                respuesta1 = "";
                respuesta4 = "";
            } else if (correcta == 3) {
                respuesta2 = "";
                respuesta3 = "";
            }
        }

        String preguntaCompleta = pregunta + "\n";
        preguntaCompleta += "respuestas" + "\n";
        preguntaCompleta += "0: " + respuesta1 + "\n";
        preguntaCompleta += "1: " + respuesta2 + "\n";
        preguntaCompleta += "2: " + respuesta3 + "\n";
        preguntaCompleta += "3: " + respuesta4 + "\n";
        preguntaCompleta += saltarCincuentaCincuenta + "\n";
        preguntaCompleta += saltarPregunta;

        return preguntaCompleta;

    }

    public static void debug() {
        System.out.println("--------------------");
        System.out.println("Pregunta actual: " + juego.preguntaActual);
        System.out.println("Pregunta saltada: " + juego.preguntaSaltada);
        System.out.println("Cincuenta cincuenta: " + juego.cincuentaCincuenta);
        System.out.println("Preguntas: " + (preguntas.obtenerNumeroPreguntas()));

        String listaPreguntas = "";
        for (int i = 0; i < preguntas.obtenerNumeroPreguntas(); i++) {
            listaPreguntas += preguntas.obtenerPregunta(i).obtenerTextoPregunta() + "\n ";
        }

        System.out.println("pull de preguntas: \n" + listaPreguntas);
        System.out.println("--------------------");

    }

}
