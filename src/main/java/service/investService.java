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
        String sql = "update financial_hub_invest set image = ?, nom_invest = ?, description_invest = ? where id = ?";
       PreparedStatement ps = connection.prepareStatement(sql);
     ps.setString(1, investment.getImage());
       ps.setString(2,investment.getNom_invest() );
       ps.setString(3, investment.getDescription_invest());
       ps.setInt(4, investment.getId());
       ps.executeUpdate();

    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from financial_hub_invest where id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();

    }

    @Override
    public List<Investment> read() throws SQLException {
        String sql = "select * from financial_hub_invest";
        Statement statement = connection.createStatement();
       ResultSet rs = statement.executeQuery(sql);
       List<Investment> investments = new ArrayList<>();
       while (rs.next()) {
            Investment investment = new Investment();
           investment.setId(rs.getInt("id"));
           investment.setImage(rs.getString("image"));
            investment.setNom_invest(rs.getString("nom_invest"));
           investment.setDescription_invest(rs.getString("description_invest"));

            investments.add(investment);
       }
        return investments;
    }


}
