package es.teis.ud2.model;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AccountMovement {

    //Fields
    private int accountMovementId;
    private Account cuentaOrigen;
    private Account cuentaDestino;
    private BigDecimal montante;
    private Timestamp fechaYHora;

    //Constructors

    public AccountMovement(int accountMovementId, Account cuentaOrigen, Account cuentaDestino, BigDecimal montante, Timestamp fechaYHora) {
        this.accountMovementId = accountMovementId;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.montante = montante;
        this.fechaYHora = fechaYHora;
    }

    public AccountMovement() {
    }

    //Other methods


    public int getAccountMovementId() {
        return accountMovementId;
    }

    public void setAccountMovementId(int accountMovementId) {
        this.accountMovementId = accountMovementId;
    }

    public Account getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Account cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Account getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Account cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public Timestamp getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(Timestamp fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    @Override
    public String toString() {
        return "AccountMovement{" +
                "accountMovementId=" + accountMovementId +
                ", cuentaOrigen=" + cuentaOrigen +
                ", cuentaDestino=" + cuentaDestino +
                ", montante=" + montante +
                ", fechaYHora=" + fechaYHora +
                '}';
    }
}
