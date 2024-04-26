package service;

import models.Investment;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class investService implements iService<Investment> {

    private Connection connection;

    public investService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Investment investment) throws SQLException {
        /*String sql = "insert into financial_hub_invest  (image,nom_invest,description_invest)" +
                "values("','" + investment.getImage() + "'" +
                "," + investment.getNom_invest() + ","+ investment.getDescription_invest()+ ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);*/

        String sql="insert into financial_hub_invest (image,nom_invest,description_invest)"+
                "Values(?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,investment.getImage());
        statement.setString(2,investment.getNom_invest());
        statement.setString(3,investment.getDescription_invest());
        statement.executeUpdate();
    }

    @Override
    public void update(Investment investment) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Investment> read() throws SQLException {
        return null;
    }


}
