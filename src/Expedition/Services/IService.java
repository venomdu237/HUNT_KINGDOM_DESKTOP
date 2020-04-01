package Expedition.Services;

import Expedition.Model.Expedition;

import java.util.List;

public interface IService<U> {
    public void ajouter(U u);
    public Expedition find(String s);
    public void modifier(U u);
    public void supprimer(U u);
    public List<U> afficher();
}
