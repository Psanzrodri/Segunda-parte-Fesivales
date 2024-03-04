package festivales.modelo;

import java.util.*;


 /**
  * Esta clase guarda una agenda con los festivales programados
  * en una serie de meses
  *
  * La agenda guardalos festivales en una colección map
  * La clave del map es el mes (un enumerado festivales.modelo.festivales.modelo.Mes)
  * Cada mes tiene asociados en una colección ArrayList
  * los festivales  de ese mes
  *
  * Solo aparecen los meses que incluyen algún festival
  *
  * Las claves se recuperan en orden alfabéico
  *
  */
public class AgendaFestivales {
    private TreeMap<Mes, ArrayList<Festival>> agenda;
    
    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }

    /**
     * añade un nuevo festival a la agenda
     *
     * Si la clave (el mes en n  el que se celebra el festival)
     * no existe en la agenda se creará una nueva entrada
     * con dicha clave y la colección formada por ese único festival
     *
     * Si la clave (el mes) ya existe se añade el nuevo festival
     * a la lista de festivales que ya existe ese ms
     * insertándolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso usa el método de ayuda
     * obtenerPosicionDeInsercion()
     *
     */
    public void addFestival(Festival festival) {
        Mes mes = festival.getMes();
        if (agenda.containsKey(mes)) {
            ArrayList<Festival> festivales = agenda.get(mes);
            int posicion = obtenerPosicionDeInsercion(festivales, festival);
            festivales.add(posicion, festival);
        } else {
            ArrayList<Festival> festivales = new ArrayList<>();
            festivales.add(festival);
            agenda.put(mes, festivales);
        }
    }

    /**
     *
     * @param festivales una lista de festivales
     * @param festival
     * @return la posición en la que debería ir el nuevo festival
     * de forma que la lista quedase ordenada por nombre
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
        String nombreFestival = festival.getNombre();
        for (int i = 0; i < festivales.size(); i++) {
            String nombre = festivales.get(i).getNombre();
            if (nombre.compareTo(nombreFestival) > 0) {
                return i;
            }
        }
        return festivales.size();
    }

    /**
     * Representación textual del festival
     * De forma eficiente
     *  Usa el conjunto de entradas para recorrer el map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Mes, ArrayList<Festival>> entry : agenda.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            ArrayList<Festival> festivales = entry.getValue();
            for (Festival festival : festivales) {
                sb.append("\t").append(festival).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param mes el mes a considerar
     * @return la cantidad de festivales que hay en ese mes
     * Si el mes no existe se devuelve -1
     */
    public int festivalesEnMes(Mes mes) {
        if (!agenda.containsKey(mes)) {
            return -1;
        }
        return agenda.get(mes).size();
    }

    /**
     * Se trata de agrupar todos los festivales de la agenda
     * por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colección
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo
     * Importa el orden de los nombres en el conjunto
     *
     * Identifica el tipo exacto del valor de retorno
     */
    public Map<String, Set<String>> festivalesPorEstilo() {
        Map<String, Set<String>> festivalesPorEstilo = new TreeMap<>();
        for (ArrayList<Festival> festivales : agenda.values()) {
            for (Festival festival : festivales) {
                String estilo = String.valueOf(festival.getEstilos());
                String nombreFestival = festival.getNombre();
                if (!festivalesPorEstilo.containsKey(estilo)) {
                    festivalesPorEstilo.put(estilo, new LinkedHashSet<>());
                }
                festivalesPorEstilo.get(estilo).add(nombreFestival);
            }
        }
        return festivalesPorEstilo;
    }

    /**
     * Se cancelan todos los festivales organizados en alguno de los
     * lugares que indica el conjunto en el mes indicado. Los festivales
     * concluidos o que no empezados no se tienen en cuenta
     * Hay que borrarlos de la agenda
     * Si el mes no existe se devuelve -1
     *
     * Si al borrar de un mes los festivales el mes queda con 0 festivales
     * se borra la entrada completa del map
     */
    public int cancelarFestivales(HashSet<String> lugares, Mes mes) {
        if (!this.agenda.containsKey(mes)) {
            return -1;
        }

        ArrayList<Festival> festivalesMes = this.agenda.get(mes);
        int numFestivalesCancelados = 0;

        for (int i = 0; i < festivalesMes.size(); i++) {
            Festival festival = festivalesMes.get(i);

            if (festival.getLugar().equals(lugares) && !festival.haConcluido()) {
                festivalesMes.remove(i);
                numFestivalesCancelados++;
                i--; //ajustamos el indice para evitar saltarnos un elemento al remover
            }
        }

        if (festivalesMes.isEmpty()) {
            this.agenda.remove(mes);
        }

        return numFestivalesCancelados;
    }
}
