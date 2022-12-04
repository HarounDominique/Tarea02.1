package es.teis.ud2.model;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AccountMovement {

    //Fields
    private int accountMovementId;
    private int cuentaOrigenId;
    private int cuentaDestinoId;
    private BigDecimal montante;
    private Timestamp fechaYHora;

    //Constructors

    public AccountMovement(int accountMovementId, int cuentaOrigenId, int cuentaDestinoId, BigDecimal montante, Timestamp fechaYHora) {
        this.accountMovementId = accountMovementId;
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
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

    public int getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(int cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public int getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(int cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
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
                ", cuentaOrigenId=" + cuentaOrigenId +
                ", cuentaDestinoId=" + cuentaDestinoId +
                ", montante=" + montante +
                ", fechaYHora=" + fechaYHora +
                '}';
    }
}
