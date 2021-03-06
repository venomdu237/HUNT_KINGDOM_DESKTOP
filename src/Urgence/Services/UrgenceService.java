package Urgence.Services;

import Expedition.Model.Expedition;
import Expedition.Services.ExpeditionService;
import Urgence.Model.Urgence;
import dataSource.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UrgenceService implements IService<Urgence> {

    Connection cnx= DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Urgence U) {
        String req="insert into urgence(expedition, utilisateur,latitude, longitude, adresse, place_id, description, plus, date, etat) values('"+U.getExpedition()+"','"+U.getUtilisateur()+"','"+U.getLatitude()+"','"+U.getLongitude()+"','"+U.getAddresse()+"','"+U.getPlace_id()+"','"+U.getDescription()+"','"+U.getPlus()+"','"+U.getDate()+"','"+U.getEtat()+"')";
        try{
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void modifier(Urgence urgence) {

    }

    @Override
    public void traiter(int id) {
        String req="update urgence SET etat=1 where id=?";
        try{
            PreparedStatement pst=cnx.prepareStatement(req);
            pst.setInt(1,id);
            pst.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Urgence U) {
        String req="delete from urgence where id=?";
        try {
            PreparedStatement pst=cnx.prepareStatement(req);
            pst.setInt(1,U.getId());
            pst.executeUpdate();
            System.out.println("Urgence Supprimée avec succès!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Urgence> afficher() {
        List<Urgence> list= new ArrayList<>();
        String req="SELECT * FROM urgence";
        try {
            PreparedStatement pst=cnx.prepareStatement(req);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                ExpeditionService Es = new ExpeditionService();
                if(rs.getInt(2) == 1)
                {
                    Urgence U=new Urgence(rs.getInt(1), "",rs.getString(3),rs.getDouble(7),rs.getDouble(8),rs.getString(4),rs.getString(9),rs.getString(5),rs.getString(6),rs.getString(10),rs.getString(11));
                    list.add(U);
                }
                else
                {
                    Urgence U=new Urgence(rs.getInt(1), Es.findName(rs.getInt(2)),rs.getString(3),rs.getDouble(7),rs.getDouble(8),rs.getString(4),rs.getString(9),rs.getString(5),"",rs.getString(10),rs.getString(11));
                    list.add(U);
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

}
