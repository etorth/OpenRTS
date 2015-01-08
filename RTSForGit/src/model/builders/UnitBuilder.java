/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.builders;

import ressources.definitions.BuilderLibrary;
import geometry.Point2D;
import geometry3D.Point3D;
import java.util.ArrayList;
import ressources.definitions.DefElement;
import java.util.HashMap;
import model.battlefield.army.ArmyManager;
import model.battlefield.actors.UnitActor;
import model.battlefield.army.components.Turret;
import model.battlefield.army.components.Unit;
import model.battlefield.army.components.Weapon;
import ressources.definitions.Definition;
import model.battlefield.warfare.Faction;

/**
 *
 * @author Benoît
 */
public class UnitBuilder extends Builder{
    static final String RADIUS = "Radius";
    static final String SEPARATION_RADIUS = "SeparationRadius";
    static final String SPEED = "Speed";
    static final String MASS = "Mass";
    static final String MOVER_LINK = "MoverLink";
    
    static final String UINAME = "UIName"; 
    static final String RACE = "Race";
    static final String MAXHEALTH = "MaxHealth";
    static final String SIGHT = "Sight";
    
    static final String WEAPONLIST = "WeaponList";
    static final String TURRET_LINK = "TurretLink";
    static final String WEAPON_LINK = "WeaponLink";
    static final String ACTOR_LINK = "ActorLink";

    private String UIName;
    private String race;
    private int maxHealth;
    private double sight;
    private String actorLink;
    private double radius;
    private double separationRadius;
    private double speed;
    private double mass;
    private String moverLink;
    private ArrayList<String> weaponLinks = new ArrayList<>();
    private ArrayList<String> turretLinks = new ArrayList<>();
    
    public UnitBuilder(Definition def, BuilderLibrary lib){
        super(def, lib);
        for(DefElement de : def.elements)
            switch(de.name){
                case RADIUS : radius = de.getDoubleVal(); break;
                case SEPARATION_RADIUS : separationRadius = de.getDoubleVal(); break;
                case SPEED : speed = de.getDoubleVal(); break;
                case MASS : mass = de.getDoubleVal(); break;
                case MOVER_LINK : moverLink = de.getVal(); break;
                    
                case UINAME : UIName = de.getVal(); break;
                case RACE : race = de.getVal(); break;
                case MAXHEALTH : maxHealth = de.getIntVal(); break;
                case SIGHT : sight = de.getDoubleVal(); break;
                case ACTOR_LINK : actorLink = de.getVal(); break;
                case WEAPONLIST :
                    weaponLinks.add(de.getVal(WEAPON_LINK));
                    turretLinks.add(de.getVal(TURRET_LINK));
            }
    }
    
    public Unit build(Faction faction, Point3D pos){
        Unit res = new Unit(radius, separationRadius, speed, mass, pos, lib.getMoverBuilder(moverLink), UIName, race, maxHealth, sight, faction, lib.getActorBuilder(actorLink));
        
        int i = 0;
        for(String weaponLink : weaponLinks){
            WeaponBuilder wb = lib.getWeaponBuilder(weaponLink);
            TurretBuilder tb = null;
            if(turretLinks.get(i) != null)
                tb = lib.getTurretBuilder(turretLinks.get(i));
            res.addWeapon(wb, tb);
            i++;
        }
        
        lib.armyManager.registerUnit(res);
        return res;
    }
    
    public boolean hasRace(String race){
        return this.race.equals(race);
    }
    
    public String getUIName(){
        return UIName;
    }
}
