import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Parcheggio parcheggio = new Parcheggio("Parcheggio Grajdeanu");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Benvenuto nel Parcheggio Grajdeanu, cosa desideri fare?:");
            System.out.println("1. Aggiungi auto");
            System.out.println("2. Cerca auto per targa");
            System.out.println("3. Cerca auto per marca");
            System.out.println("4. Visualizza tutte le auto");
            System.out.println("5. Rimuovi auto");
            System.out.println("6. Esci");
            System.out.print("Scegli un'opzione: ");
            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    System.out.print("Inserisci targa: ");
                    String targa = scanner.nextLine();
                    System.out.print("Inserisci marca: ");
                    String marca = scanner.nextLine();
                    System.out.print("Inserisci modello: ");
                    String modello = scanner.nextLine();
                    Auto auto = new Auto(targa,marca,modello);
                    if (parcheggio.aggiungiAuto(targa, marca, modello) || parcheggio.inserisciAutoSulDatabase(auto)) {
                            System.out.println("----Auto aggiunta!-----");
                    }
                    break;

                case 2:
                    System.out.print("Inserisci targa da cercare: ");
                    targa = scanner.nextLine();
                    auto = parcheggio.cercaAutoPerTarga(targa);
                    if (auto != null) {
                        System.out.println("----Auto trovata: " + auto + "-----");
                    } else {
                        System.out.println("----Auto non trovata.----");
                    }
                    break;

                case 3:
                    System.out.print("Inserisci marca da cercare: ");
                    marca = scanner.nextLine();
                    String autoPerMarca = parcheggio.cercaAutoPerMarca(marca);
                    if (!autoPerMarca.isEmpty()) {
                        System.out.println("Auto trovate:\n" + autoPerMarca);
                    } else {
                        System.out.println("-----Nessuna auto trovata per questa marca.-----");
                    }
                    break;

                case 4:
                    System.out.println("Auto nel parcheggio:");
                    System.out.println(parcheggio);
                    break;

                case 5:
                    System.out.print("Inserisci targa dell'auto da eliminare");
                    targa = scanner.nextLine();
                    if(parcheggio.rimuoviAuto(targa) || parcheggio.rimuoviAutoDalDatabase(targa)) {
                        System.out.println("---Auto eliminata");
                    }
                    break;
                case 6:
                    running= false;
                    break;

                default:
                    System.out.println("----Scelta non valida.-----");
            }
        }

        scanner.close();
    }
}
