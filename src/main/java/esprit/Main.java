package esprit;


import models.Investment;
import service.investService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        investService is = new investService();
        try {
            is.create(new Investment("iii","name","descrition"));
            is.delete(28);
            is.update(new Investment(26,"anwer","yahyaoui","mohamed"));
            System.out.println(is.read());

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }

    }
}