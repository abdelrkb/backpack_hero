package model;

import java.util.ArrayList;
import java.util.Random;

import sae.enemy.Enemy;
import sae.enemy.FrogWizard;
import sae.enemy.LivingShadow;
import sae.enemy.QueenBee;
import sae.enemy.RatWolf;
import sae.enemy.SmallRatWolf;
import sae.equipment.Bag;
import sae.equipment.Equipment;
import sae.equipment.Gold;
import sae.equipment.Other;
import sae.equipment.Protection;
import sae.equipment.weapon.MeleeWeapon;
import sae.equipment.weapon.RangeWeapon;
import sae.hero.Hero;
import sae.map.Map;
import sae.npc.Healer;

public class SimpleGameData  {
	//Data de jeu
	public static Map map = new Map(); //Cree la map
	public static int nbFloor = 0;  //Compte le nombre d'étages parcourus
	public static int energy = 10; //Energie pour les combats
	public static Bag bag = new Bag();//Cree le sac
	public static int CurrRows = 0;//Garde en memoise la position x du personnage sur la map
	public static int CurrCols = 0;//Garde en memoise la position y du personnage sur la map
	public static Hero hero = new Hero();//Cree le hero
	public static Healer healer = new Healer();//cree le healer
	

	//Evenements
	public static boolean printMap = false;//Pour savoir si la map est affiché ou non
	public static boolean healerEvent = false;//pour savoir sil'évenement healer esr en cours ou non
	public static boolean numbHeal= false;//vérifie si le heal a déja été attribué ou pas
	public static boolean treasureEvent = false;//pour savoir si l'événement tresor est en cours
	public static boolean EnemyEventDown = false;//savoir si l'event enemy est en cours pour une case
	public static boolean EnemyEventRight = false;////savoir si l'event enemy est en cours pour une case
	public static boolean EnemyEventUp = false;//savoir si l'event enemy est en cours pour une case
	public static boolean EnemyEventLeft = false;//savoir si l'event enemy est en cours pour une case
	public static boolean tourEvent = true;//savoir a qui il est le tour
	public static boolean MerchantEvent = false;//est ce que l'événement marchant est en cours
	public static int cmptMerchant = 0;//savoir si un item a été acheté ou pas

	public static Random random = new Random();//génère un nombre random
	//items
	

	public static Equipment woodenSword = new MeleeWeapon("Wooden Sword", 7, 4, 5, "common", null, 0, 3, 1);//epee en bois
	public static float swordX = 1000;//position x pour le tresor
	public static float swordY = 100;//position y pour le tresor
	public static boolean dragSword = false;//a-t-on commencer a drag&drop l'épée

	public static Equipment woodenShield = new Protection("Wooden Shield", 3, 5, 5, "common", null, 0,2);
	public static float shieldX = 370;//position x pour le tresor
	public static float shieldY = 100;//position y pour le tresor
	public static boolean dragShield = false;//a-t-on commencer a drag&drop le shield

	public static Equipment food = new Other("steak", 2, 3, 5, 3);//nourriture 
	public static Equipment bow = new RangeWeapon("Bow", 6, 4, 6, "common", 0, 4);//arc
	public static Gold gold = new Gold(5);//gold

	//Enemy
	public static Enemy smallRat = new SmallRatWolf();
	public static Enemy rat = new RatWolf();
	public static Enemy queenBee = new QueenBee();
	public static Enemy livingShadow = new LivingShadow();
	public static Enemy frogWizard= new FrogWizard();

}
