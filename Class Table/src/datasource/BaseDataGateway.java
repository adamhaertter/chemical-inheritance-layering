package datasource;

public class BaseDataGateway extends Gateway {

    private long id = 0;
    private int solute = 0;

   /** only used to create a data gateway for base **/
    BaseDataGateway(long id) {
        id = this.id;
        deleted = false;
    }

    /** used to create a new base **/
    BaseDataGateway(int solute) {
        solute = this.solute;
        deleted = false;
    }
}