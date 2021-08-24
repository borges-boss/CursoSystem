package cursosystem;

public class sessionUser {

    private static String nomeUser;
    private static String idUnidade;
    private static int idCliente = 0;//O ID DO CLIENTE E USADO PARA FAZER A ATUALIZAÇAO NO BD LOCAL
    private static int idClienteAgenda = 0;//O ID SERA USADO PARA FAZER A INSERÇAO NO AGENDAMENTO
    private static String nomeClienteAgenda;

    sessionUser() {

    }

    public String getNameUser() {
        return nomeUser;
    }

    public void setNameUser(String nomeUser) {
        sessionUser.nomeUser = nomeUser;

    }

    public String getidUnidade() {
        return idUnidade;
    }

    public void setidUnidade(String idUnidade) {
        sessionUser.idUnidade = idUnidade;

    }

    public String getNomeAgenda() {
        return nomeClienteAgenda;
    }

    public void setNomeAgenda(String nomeClienteAgenda) {
        sessionUser.nomeClienteAgenda = nomeClienteAgenda;

    }

    public int getIdClienteAgenda() {
        return idClienteAgenda;

    }

    public void setIdClienteAgenda(int idCliente) {
        this.idClienteAgenda = idCliente;

    }

    public int getIdCliente() {
        return idCliente;

    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;

    }

}
