package festivales.io;

import festivales.modelo.AgendaFestivales;
import festivales.modelo.Estilo;
import festivales.modelo.Festival;

import java.util.HashSet;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * La clase contiene méodos estáticos que permiten
 * cargar la agenda de festivales leyendo los datos desde
 * un fichero
 */
public class FestivalesIO {

    
    public static void cargarFestivales(AgendaFestivales agenda) {
        Scanner sc = null;
        try {
            sc = new Scanner(FestivalesIO.class.getResourceAsStream("/festivales.csv"));
            while (sc.hasNextLine()) {
                String lineaFestival = sc.nextLine();
                Festival festival = parsearLinea(lineaFestival);
                agenda.addFestival(festival);
                
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        
    }

    /**
     * se parsea la línea extrayendo sus datos y creando y
     * devolviendo un objeto festivales.modelo.Festival
     * @param lineaFestival los datos de un festival
     * @return el festival creado
     */
    public static Festival parsearLinea(String lineaFestival) {
        //Troceamos la línea
        String[] trozos = lineaFestival.split(":");

        //Vamos recogiendo cada dato en una variable adecuada para lo que necesitaremos
        String nombre = capitaliza(trozos[0].trim());
        String lugar = trozos[1].trim().toUpperCase();
        LocalDate fecha = ConversionFecha.parsearFecha(trozos[2].trim(),"dd-MM-yyyy");
        int duracion = Integer.parseInt(trozos[3].trim());

        //Preparamos el HashSet de elementos festivales.modelo.Estilo con los elementos desde la posición 4 en adelante
        HashSet<Estilo> setEstilos = new HashSet<>();
        for (int i = 4; i < trozos.length; i++) {
            Estilo e = Estilo.valueOf(trozos[i].trim().toUpperCase());
            setEstilos.add(e);
        }

        //Ya tenemos todos los datos necesarios para crear el festivales.modelo.Festival
        Festival fest = new Festival(nombre, lugar, fecha, duracion, setEstilos);
        return fest;
    }


    /**
     * Devuelve el texto recibido capitalizado (primera letra en mayúscula y el resto en minúsculas)
     * @param texto String: el texto a capitalizar
     * @return String: el texto capitalizado
     */
    private static String capitaliza(String texto) {
        String[] palabras = texto.split("[,.\s]+");
        StringBuilder capitalizada = new StringBuilder();
        for (String pal : palabras) {
            String mayus = pal.substring(0, 1).toUpperCase() + pal.substring(1).toLowerCase();
            capitalizada.append(mayus).append(" ");
        }
        return capitalizada.toString().trim();
    }


    public static class ConversionFecha {
        public static LocalDate parsearFecha(String fecha, String formato) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return LocalDate.parse(fecha, formatter);
        }
    }
}
