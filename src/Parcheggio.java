import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import java.sql.*;

public class Parcheggio {
    private String nome;
    private ArrayList<Auto> listaAuto;


    private static final String filePath = "ParcheggioT.txt";

    private final String DB_url = "jdbc:mysql://localhost:3306/dbesempio" ;
    private final String DB_username = "root";
    private final String DB_password = "";


    public Parcheggio(String nome) {
        this.nome = nome;
        this.listaAuto = new ArrayList<>();
        //inserisciAutoDaFile();
        //inserisciAutoDalDatabase();
    }

    public String getNome() {
        return nome;
    }

    public boolean aggiungiAuto(String targa, String marca, String modello) throws AlreadyPresentPlateException {
        try {
            if (cercaAutoPerTarga(targa) != null) {
                throw new AlreadyPresentPlateException("\nLa targa " + targa + " è già presente nel parcheggio");
            }
            Auto a = new Auto(targa, marca, modello);
            listaAuto.add(a);
            inserisciAutoSuFile(a);
            inserisciAutoSulDatabase(a);
        } catch (IllegalArgumentException | AlreadyPresentPlateException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("\nImpossibile aprire/chiudere il file");
        }
        return true;
    }

    public boolean rimuoviAuto(String targa) {
        Auto a = cercaAutoPerTarga(targa);
        if (a != null) {
            return listaAuto.remove(a) && rimuoviAutoDaFile(targa) && rimuoviAutoDalDatabase(targa);
        }
        return false;
    }

    public Auto cercaAutoPerTarga(String targa) {
        for (Auto a : listaAuto) {
            if (a.getTarga().equals(targa.trim()))
                return a;
        }
        return null;
    }

    public String cercaAutoPerMarca(String marca) {
        StringBuilder sb = new StringBuilder();
        for (Auto a : listaAuto) {
            if (a.getMarca().equalsIgnoreCase(marca.trim()))
                sb.append(a + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Auto a : listaAuto)
            sb.append(a + "\n");
        return sb.toString();
    }

    private void inserisciAutoDaFile(){
        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("File non trovato: " + filePath);
            return;
        }
        try {
            List<String> righe = Files.readAllLines(Paths.get(filePath));
            for (String riga : righe) {
                String[] dettagli = riga.split(",");
                String targa = dettagli[0].trim();
                String marca = dettagli[1].trim();
                String modello = dettagli[2].trim();
                aggiungiAuto(targa, marca, modello);
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura del file: " + e.getMessage(), e);
        }
    }

    private boolean inserisciAutoSuFile(Auto auto) throws IOException {
        List<String> righe = Files.readAllLines(Paths.get(filePath));
        for (String riga : righe) {
            if (riga.contains(auto.getTarga())) {
                return false;
            }
        }
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(auto.getTarga() + " , " + auto.getMarca() + " , " + auto.getModello() + "\n");
        }
        return true;
    }

    private boolean rimuoviAutoDaFile(String targa){
        try {
            List<String> righe = Files.readAllLines(Paths.get(filePath));
            try (FileWriter writer = new FileWriter(filePath)) {
                for (String riga : righe) {
                    if (!riga.contains(targa)) {
                        writer.write(riga + System.lineSeparator());
                    }
                }
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'auto: " + e.getMessage(), e);
        }
    }




    private void inserisciAutoDalDatabase() {
        String query = "SELECT Targa, Marca, Modello FROM auto";

        try (Connection connection = DriverManager.getConnection(DB_url, DB_username, DB_password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                aggiungiAuto(resultSet.getString("Targa"), resultSet.getString("Marca"), resultSet.getString("Modello"));
            }

        } catch (SQLException e) {
            System.out.println("Errore di apertura/lettura del database:\n" + e.getMessage());
        }
    }


    public boolean inserisciAutoSulDatabase(Auto auto) {
        String insertQuery = "INSERT INTO auto (Targa, Marca, Modello) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_url, DB_username, DB_password);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {


            insertStatement.setString(1, auto.getTarga());
            insertStatement.setString(2, auto.getMarca());
            insertStatement.setString(3, auto.getModello());

            int rowsInserted = insertStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Errore di apertura/scrittura del database:\n" + e.getMessage());
            return false;
        }
    }

    public boolean rimuoviAutoDalDatabase(String targa) {
        String deleteQuery = "DELETE FROM auto WHERE Targa = ?";

        try (Connection connection = DriverManager.getConnection(DB_url, DB_username, DB_password);
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, targa);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Errore di apertura/rimozioneEntry del database:\n" + e.getMessage());
            return false;
        }
    }






}
