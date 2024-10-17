public class Auto {
    private String targa;
    private String marca;
    private String modello;


    public Auto(String targa, String marca, String modello) {
        if (controlloTarga(targa)) {
            this.targa = targa;
            this.marca = marca;
            this.modello = modello;
        } else {
            throw new IllegalArgumentException("La targa " + targa + " non è valida");
        }
    }

    public String getTarga() {
        return targa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public boolean controlloTarga(String targa) {
        String targaRegex = "^[A-Z]{2}\\d{3}[A-Z]{2}$";
        return targa.matches(targaRegex);
    }

    @Override
    public String toString() {
        return "Targa: " + targa + ", Marca: " + marca + ", Modello: " + modello;
    }
}
