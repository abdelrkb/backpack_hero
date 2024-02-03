package controller;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.KeyboardKey;
import model.SimpleGameData;
import sae.enemy.FrogWizard;
import sae.enemy.QueenBee;
import sae.enemy.RatWolf;
import sae.enemy.SmallRatWolf;
import sae.map.Map;

import vue.SimpleGameView;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.Event;

public class SimpleGameController {

	public SimpleGameController() {

	}

	public static void backpackHero(ApplicationContext context) {
		System.out.println(SimpleGameData.map);
		SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		
		System.out.println(SimpleGameData.bag);
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			var action = event.getAction();
			var location = event.getLocation();
			//Si on clique sur M une fois on affiche la map et si on réappuie on l'efface
			if (action == Action.KEY_PRESSED) {
				if (event.getKey() == KeyboardKey.M) {
					if (SimpleGameData.printMap == false) {
						SimpleGameView.drawMap(context, SimpleGameData.map, SimpleGameData.CurrRows,
								SimpleGameData.CurrCols);
						//remet les evenements a 0 a chaque fois qu'on rapppuie sur la map
						SimpleGameData.printMap = true;
						SimpleGameData.treasureEvent = false;
						SimpleGameData.EnemyEventDown = false;
						SimpleGameData.EnemyEventUp = false;
						SimpleGameData.EnemyEventRight = false;
						SimpleGameData.EnemyEventLeft = false;
						SimpleGameData.MerchantEvent = false;
						SimpleGameData.tourEvent = true;
						SimpleGameData.cmptMerchant = 0;
					} else {
						SimpleGameView.deleteMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
					}

				}
			}

			
			
			
			
			//condition de fin si le héro meur
			else if (SimpleGameData.hero.getLife() <= 0) {
				SimpleGameView.gameOver(context);
			}
			
			
			// Le joueur se déplace vers le bas
			else if (event.getKey() == KeyboardKey.DOWN && SimpleGameData.printMap == true) {
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows + 1, SimpleGameData.CurrCols) != 0
						&& (SimpleGameData.CurrRows + 1) <= 5) {

					SimpleGameData.CurrRows += 1;
					SimpleGameView.drawMap(context, SimpleGameData.map, SimpleGameData.CurrRows,
							SimpleGameData.CurrCols);
					SimpleGameView.heroMap(context, SimpleGameData.CurrRows, SimpleGameData.CurrCols);
				}

				// Le joueur arrive sur un healer
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 3) {
					SimpleGameView.healerMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
					SimpleGameData.printMap = false;
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.healerEvent = true;
					SimpleGameData.numbHeal = true;
				}
				// Le joueur arrive sur un ennemi
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 4) {
					SimpleGameData.queenBee = new QueenBee();
					SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
							SimpleGameData.gold, SimpleGameData.energy);
					SimpleGameData.printMap = false;
					SimpleGameData.EnemyEventDown = true;
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
				}

				// Le joueur arrive sur un tresor
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 5) {
					SimpleGameView.treasureMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
					SimpleGameData.printMap = false;
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.treasureEvent = true;
				}

				// Le joueur arrive sur un marchand
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 6) {
					SimpleGameView.merchantMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
					SimpleGameData.printMap = false;
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.MerchantEvent = true;
				}

				// Le joueur arrive a la sortie de l'étage
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 7) {
					SimpleGameData.nbFloor++;
					if (SimpleGameData.nbFloor > 2) {
						context.exit(0);
						return;
					}
					SimpleGameView.deleteMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
					SimpleGameData.printMap = false;
					SimpleGameData.map = new Map();
					SimpleGameData.CurrRows = 0;
					SimpleGameData.CurrCols = 0;
				}

			}

			// EVENEMENT HEALER
			else if (event.getKey() == KeyboardKey.H && SimpleGameData.printMap == false
					&& SimpleGameData.healerEvent == true) {
				if (SimpleGameData.numbHeal == true) {
					SimpleGameData.healer.giveHeal(SimpleGameData.hero);
					SimpleGameView.lifeHero(context, SimpleGameData.hero);
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.numbHeal = false;
					SimpleGameData.healerEvent = false;
					SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
				} else {
					continue;
				}
			}
			// EVENT TREASURE
			else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				System.out.println(location.x);
				System.out.println(location.y);
				if (action == Action.POINTER_DOWN) {
					if (location.x > 1082 && location.x < 1128 && location.y > 115 && location.y < 197) {
						SimpleGameData.dragSword = true;
					} else if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}
			} else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
					if (action == Action.POINTER_DOWN) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}

			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragShield == true) {
				// Case 1
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(0, 1) == 0
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;
				}
				// Case 2
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(0, 2) == 0
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

					// Case 3
				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(0, 3) == 0
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 4
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(0, 4) == 0
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 5
				if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0 && SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 6

				if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0 && SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 7

				if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0 && SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}

				// Case 8

				if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0 && SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			}

			else if (action == Action.POINTER_DOWN && SimpleGameData.dragSword == true) {
				// Case 1
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(1, 0) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragSword = false;
				}
				// case 2
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				// case 3
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragSword = false;

				}
				// case 4
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragSword = false;

				}
				// case 5
				if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 4) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragSword = false;

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			}

			// Combat
			else if (action == Action.POINTER_DOWN && SimpleGameData.EnemyEventDown == true) {
				{
					if (SimpleGameData.hero.getLife() > 0 && SimpleGameData.queenBee.getLifePoint() > 0) {
						if (SimpleGameData.energy > 0) {
							if (action == Action.POINTER_DOWN) {
								//Si le joueur clique sur la case numéro 1 de son sac a dos
								if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131) {
									//Si la case est une épée
									if (SimpleGameData.bag.getIndex(0,0) == 1) {
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								}
								//Si le joueur clique sur la case numéro 2
								if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,1) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);									
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								}
								//Si le joueur clique sur la case numéro 3
								if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,2) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}	
								//Si le joueur clique sur la case numéro 4
								if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,3) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 5
								if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,4) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 6
								if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,0) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 7
								if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,1) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 8
								if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,2) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 9
								if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,3) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 10
								if (location.x > 826 && location.x < 887 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,4) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 11
								if (location.x > 521 && location.x < 594 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,0) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 12
								if (location.x > 601 && location.x < 665 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,1) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 13
								if (location.x > 672 && location.x < 735 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,2) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 14
								if (location.x > 749 && location.x < 815 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,3) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							
								//Si le joueur clique sur la case numéro 15
								if (location.x > 826 && location.x < 887 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,4) == 1) {
										//Si la case est une épée
										SimpleGameData.queenBee.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.queenBee,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							}
						}
						else if(SimpleGameData.energy <= 0){
							
							int randomChoice = SimpleGameData.random.nextInt(2) + 1; 
							if (randomChoice == 1) {
					            System.out.println("L'ennemi attaque !");
					            SimpleGameData.hero.receiveDamage(SimpleGameData.queenBee);
					            SimpleGameView.lifeHero(context, SimpleGameData.hero);
					        } else if (randomChoice == 2) {
					        	System.out.println("L'ennemi se défend !");
					            SimpleGameData.queenBee.receiveProtection();
					        }
							
							SimpleGameData.energy = 10;
						}
					}
					
					else if(SimpleGameData.hero.getLife() <=0){
						System.out.println("combat fini");
						SimpleGameView.gameOver(context);
						context.exit(0);
						return;
					}
					else if(SimpleGameData.queenBee.getLifePoint()<=0) {
						SimpleGameData.gold.increaseGold(5);
						SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.energy = 10;
						SimpleGameData.EnemyEventDown = false;}
				}
			}
			else if (event.getKey() == KeyboardKey.B && SimpleGameData.MerchantEvent == true) {
				for (int rows = 0; rows<3;rows++) {
					for (int cols = 0; cols < 5; cols++) {
						if (SimpleGameData.bag.getIndex(rows,cols) == 0  && SimpleGameData.cmptMerchant == 0) {
							SimpleGameData.bag.changeIndex(rows, cols, SimpleGameData.food.itemGetId());
							SimpleGameData.gold.decreaseGold(SimpleGameData.food.itemGetPrice());
							System.out.println(SimpleGameData.bag);
							SimpleGameView.drawBackpack(context, SimpleGameData.bag);
							SimpleGameData.cmptMerchant++;
						}
						else {
							continue;
						}
						SimpleGameData.MerchantEvent = false;
					}
				}
				
			}
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//
			
			// Le joueur se déplace vers la droite
			else if (event.getKey() == KeyboardKey.RIGHT && SimpleGameData.printMap == true) {
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols + 1) != 0
						&& ((SimpleGameData.CurrCols + 1) <= 11)) {

					SimpleGameData.CurrCols += 1;
					SimpleGameView.drawMap(context, SimpleGameData.map, SimpleGameData.CurrRows,
							SimpleGameData.CurrCols);
					SimpleGameView.heroMap(context, SimpleGameData.CurrRows, SimpleGameData.CurrCols);
					// Le joueur arrive sur un healer
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 3) {
						SimpleGameView.healerMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.healerEvent = true;
						SimpleGameData.numbHeal = true;
					}
					// Le joueur arrive sur un ennemi
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 4) {
						SimpleGameData.smallRat = new SmallRatWolf();
						SimpleGameData.EnemyEventRight = true;
						SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.smallRat, SimpleGameData.gold, SimpleGameData.energy);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					}

					// Le joueur arrive sur un tresor
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 5) {
						SimpleGameView.treasureMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.treasureEvent = true;
					}
					// Le joueur arrive sur un marchand
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 6) {
						SimpleGameView.merchantMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.MerchantEvent = true;
					}

					// Le joueur arrive a la sortie de l'étage
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 7) {
						SimpleGameData.nbFloor++;
						if (SimpleGameData.nbFloor > 2) {
							context.exit(0);
							return;
						}
						SimpleGameView.deleteMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map = new Map();
						SimpleGameData.CurrRows = 0;
						SimpleGameData.CurrCols = 0;
					}
				}
			} else if (event.getKey() == KeyboardKey.H && SimpleGameData.printMap == false
					&& SimpleGameData.healerEvent == true) {
				if (SimpleGameData.numbHeal == true) {
					SimpleGameData.healer.giveHeal(SimpleGameData.hero);
					SimpleGameView.lifeHero(context, SimpleGameData.hero);
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.numbHeal = false;
					SimpleGameData.healerEvent = false;
					SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
				} else {
					continue;
				}
			}
			// EVENT TREASURE
			else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				System.out.println(location.x);
				System.out.println(location.y);
				if (action == Action.POINTER_DOWN) {
					if (location.x > 1082 && location.x < 1128 && location.y > 115 && location.y < 197) {
						SimpleGameData.dragSword = true;
					} else if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}
			} else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
					if (action == Action.POINTER_DOWN) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}

			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragShield == true) {
				// Case 1
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(0, 1) == 0
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;
				}
				// Case 2
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(0, 2) == 0
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

					// Case 3
				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(0, 3) == 0
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 4
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(0, 4) == 0
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 5
				if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0 && SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 6

				if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0 && SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 7

				if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0 && SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}

				// Case 8

				if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0 && SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragSword == true) {
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(1, 0) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 4) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}

				else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			}
			//Event COMBAT
			else if (action == Action.POINTER_DOWN && SimpleGameData.EnemyEventRight == true) {
				{
					if (SimpleGameData.hero.getLife() > 0 && SimpleGameData.smallRat.getLifePoint() > 0) {
						if (SimpleGameData.energy > 0) {
							if (action == Action.POINTER_DOWN) {
								//Si le joueur clique sur la case numéro 1 de son sac a dos
								if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131) {
									//Si la case est une épée
									if (SimpleGameData.bag.getIndex(0,0) == 1) {
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								}
								//Si le joueur clique sur la case numéro 2
								if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,1) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);									
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 3
								if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,2) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}	
								//Si le joueur clique sur la case numéro 4
								if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,3) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 5
								if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,4) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 6
								if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,0) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 7
								if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,1) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 8
								if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,2) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 9
								if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,3) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 10
								if (location.x > 826 && location.x < 887 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,4) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 11
								if (location.x > 521 && location.x < 594 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,0) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 12
								if (location.x > 601 && location.x < 665 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,1) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 13
								if (location.x > 672 && location.x < 735 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,2) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 14
								if (location.x > 749 && location.x < 815 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,3) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							
								//Si le joueur clique sur la case numéro 15
								if (location.x > 826 && location.x < 887 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,4) == 1) {
										//Si la case est une épée
										SimpleGameData.smallRat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.smallRat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							}
						}
						else if(SimpleGameData.energy <= 0){
							
							int randomChoice = SimpleGameData.random.nextInt(2) + 1; 
							if (randomChoice == 1) {
					            System.out.println("L'ennemi attaque !");
					            SimpleGameData.hero.receiveDamage(SimpleGameData.smallRat);
					            SimpleGameView.lifeHero(context, SimpleGameData.hero);
					        } else if (randomChoice == 2) {
					        	System.out.println("L'ennemi se défend !");
					            SimpleGameData.smallRat.receiveProtection();
					        }
							
							SimpleGameData.energy = 10;
						}
					}
					
					else if(SimpleGameData.hero.getLife() <=0){
						System.out.println("combat fini");
						SimpleGameView.gameOver(context);
						context.exit(0);
						return;
					}
					else if(SimpleGameData.smallRat.getLifePoint()<=0) {
						SimpleGameData.gold.increaseGold(5);
						SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.energy = 10;
						SimpleGameData.EnemyEventRight = false;}
				}
			}
			else if (event.getKey() == KeyboardKey.B && SimpleGameData.MerchantEvent == true) {
				for (int rows = 0; rows<3;rows++) {
					for (int cols = 0; cols < 5; cols++) {
						if (SimpleGameData.bag.getIndex(rows,cols) == 0  && SimpleGameData.cmptMerchant == 0) {
							SimpleGameData.bag.changeIndex(rows, cols, SimpleGameData.food.itemGetId());
							SimpleGameData.gold.decreaseGold(SimpleGameData.food.itemGetPrice());
							System.out.println(SimpleGameData.bag);
							SimpleGameView.drawBackpack(context, SimpleGameData.bag);
							SimpleGameData.cmptMerchant++;
						}
						else {
							continue;
						}
						SimpleGameData.MerchantEvent = false;
					}
				}
				
			}
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//
			// Le joueur se déplace vers la gauche
			else if (event.getKey() == KeyboardKey.LEFT && SimpleGameData.printMap == true) {
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols - 1) != 0
						&& (SimpleGameData.CurrCols - 1) >= 0) {

					SimpleGameData.CurrCols -= 1;
					SimpleGameView.drawMap(context, SimpleGameData.map, SimpleGameData.CurrRows,
							SimpleGameData.CurrCols);
					SimpleGameView.heroMap(context, SimpleGameData.CurrRows, SimpleGameData.CurrCols);
					// Le joueur arrive sur un healer
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 3) {
						SimpleGameView.healerMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.healerEvent = true;
						SimpleGameData.numbHeal = true;
					}
					// Le joueur arrive sur un ennemi
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 4) {
						SimpleGameData.rat = new RatWolf();
						SimpleGameData.EnemyEventLeft = true;
						SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
								SimpleGameData.gold, SimpleGameData.energy);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					}
					// Le joueur arrive sur un tresor
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 5) {
						SimpleGameView.treasureMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.treasureEvent = true;

					}
					// Le joueur arrive sur un marchand
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 6) {
						SimpleGameView.merchantMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.MerchantEvent = true;
					}

					// Le joueur arrive a la sortie de l'étage
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 7) {
						SimpleGameData.nbFloor++;
						if (SimpleGameData.nbFloor > 2) {
							context.exit(0);
							return;
						}
						SimpleGameView.deleteMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map = new Map();
						SimpleGameData.CurrRows = 0;
						SimpleGameData.CurrCols = 0;
					}

				}
			} else if (event.getKey() == KeyboardKey.H && SimpleGameData.printMap == false
					&& SimpleGameData.healerEvent == true) {
				if (SimpleGameData.numbHeal == true) {
					SimpleGameData.healer.giveHeal(SimpleGameData.hero);
					SimpleGameView.lifeHero(context, SimpleGameData.hero);
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.numbHeal = false;
					SimpleGameData.healerEvent = false;
					SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
				} else {
					continue;
				}
			}
			// EVENT TREASURE
			else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				System.out.println(location.x);
				System.out.println(location.y);
				if (action == Action.POINTER_DOWN) {
					if (location.x > 1082 && location.x < 1128 && location.y > 115 && location.y < 197) {
						SimpleGameData.dragSword = true;
					} else if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}
			} else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
					if (action == Action.POINTER_DOWN) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}

			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragShield == true) {
				// Case 1
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(0, 1) == 0
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;
				}
				// Case 2
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(0, 2) == 0
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

					// Case 3
				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(0, 3) == 0
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 4
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(0, 4) == 0
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 5
				if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0 && SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 6

				if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0 && SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 7

				if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0 && SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}

				// Case 8

				if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0 && SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragSword == true) {
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(1, 0) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 4) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			}
			//Event COMBAT
			else if (action == Action.POINTER_DOWN && SimpleGameData.EnemyEventLeft == true) {
				{
					if (SimpleGameData.hero.getLife() > 0 && SimpleGameData.rat.getLifePoint() > 0) {
						if (SimpleGameData.energy > 0) {
							if (action == Action.POINTER_DOWN) {
								//Si le joueur clique sur la case numéro 1 de son sac a dos
								if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131) {
									//Si la case est une épée
									if (SimpleGameData.bag.getIndex(0,0) == 1) {
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
									
								}
								//Si le joueur clique sur la case numéro 2
								if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,1) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);									
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
								
								}
								//Si le joueur clique sur la case numéro 3
								if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,2) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
								
								}	
								//Si le joueur clique sur la case numéro 4
								if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,3) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
								
								}
								//Si le joueur clique sur la case numéro 5
								if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,4) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
								
								}
								//Si le joueur clique sur la case numéro 6
								if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,0) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
								
								}
								//Si le joueur clique sur la case numéro 7
								if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,1) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 8
								if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,2) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								}
								//Si le joueur clique sur la case numéro 9
								if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,3) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 10
								if (location.x > 826 && location.x < 887 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,4) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(1, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 11
								if (location.x > 521 && location.x < 594 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,0) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 12
								if (location.x > 601 && location.x < 665 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,1) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 13
								if (location.x > 672 && location.x < 735 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,2) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
								
								}
								//Si le joueur clique sur la case numéro 14
								if (location.x > 749 && location.x < 815 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,3) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							
								//Si le joueur clique sur la case numéro 15
								if (location.x > 826 && location.x < 887 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,4) == 1) {
										//Si la case est une épée
										SimpleGameData.rat.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.rat,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(2, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							}
						}
						else if(SimpleGameData.energy <= 0){
							
							int randomChoice = SimpleGameData.random.nextInt(2) + 1; 
							if (randomChoice == 1) {
					            System.out.println("L'ennemi attaque !");
					            SimpleGameData.hero.receiveDamage(SimpleGameData.rat);
					            SimpleGameView.lifeHero(context, SimpleGameData.hero);
					        } else if (randomChoice == 2) {
					        	System.out.println("L'ennemi se défend !");
					            SimpleGameData.rat.receiveProtection();
					        }
							
							SimpleGameData.energy = 10;
						}
					}
					
					else if(SimpleGameData.hero.getLife() <=0){
						System.out.println("combat fini");
						SimpleGameView.gameOver(context);
						context.exit(0);
						return;
					}
					else if(SimpleGameData.rat.getLifePoint()<=0) {
						SimpleGameData.gold.increaseGold(5);
						SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.energy = 10;
						SimpleGameData.EnemyEventLeft = false;}
				}}
			else if (event.getKey() == KeyboardKey.B && SimpleGameData.MerchantEvent == true) {
				for (int rows = 0; rows<3;rows++) {
					for (int cols = 0; cols < 5; cols++) {
						if (SimpleGameData.bag.getIndex(rows,cols) == 0  && SimpleGameData.cmptMerchant == 0) {
							SimpleGameData.bag.changeIndex(rows, cols, SimpleGameData.food.itemGetId());
							SimpleGameData.gold.decreaseGold(SimpleGameData.food.itemGetPrice());
							System.out.println(SimpleGameData.bag);
							SimpleGameView.drawBackpack(context, SimpleGameData.bag);
							SimpleGameData.cmptMerchant++;
						}
						else {
							continue;
						}
						SimpleGameData.MerchantEvent = false;
					}
				}
				
			}
			
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//
			//

			// Le joueur se déplace vers le haut
			else if (event.getKey() == KeyboardKey.UP && SimpleGameData.printMap == true) {
				if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows - 1, SimpleGameData.CurrCols) != 0
						&& (SimpleGameData.CurrRows - 1) >= 0) {

					SimpleGameData.CurrRows -= 1;
					SimpleGameView.drawMap(context, SimpleGameData.map, SimpleGameData.CurrRows,
							SimpleGameData.CurrCols);
					SimpleGameView.heroMap(context, SimpleGameData.CurrRows, SimpleGameData.CurrCols);

					// Le joueur arrive sur un healer
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 3) {
						SimpleGameView.healerMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.healerEvent = true;
						SimpleGameData.numbHeal = true;
					}
					// Le joueur arrive sur un ennemi
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 4) {
						SimpleGameData.frogWizard = new FrogWizard();
						SimpleGameData.EnemyEventUp = true;
						SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.frogWizard, SimpleGameData.gold, SimpleGameData.energy);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					}

					// Le joueur arrive sur un tresor
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 5) {
						SimpleGameView.treasureMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.treasureEvent = true;

					}

					// Le joueur arrive sur un marchand
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 6) {
						SimpleGameView.merchantMap(context, SimpleGameData.hero, SimpleGameData.bag,
								SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
						SimpleGameData.MerchantEvent = true;
					}
					// Le joueur arrive a la sortie de l'étage
					if (SimpleGameData.map.getIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols) == 7) {
						SimpleGameData.nbFloor++;
						if (SimpleGameData.nbFloor > 2) {
							context.exit(0);
							return;
						}
						SimpleGameView.deleteMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.printMap = false;
						SimpleGameData.map = new Map();
						SimpleGameData.CurrRows = 0;
						SimpleGameData.CurrCols = 0;
					}

				}

			} else if (event.getKey() == KeyboardKey.H && SimpleGameData.printMap == false
					&& SimpleGameData.healerEvent == true) {
				if (SimpleGameData.numbHeal == true) {
					SimpleGameData.healer.giveHeal(SimpleGameData.hero);
					SimpleGameView.lifeHero(context, SimpleGameData.hero);
					SimpleGameData.map.changeIndex(SimpleGameData.CurrRows, SimpleGameData.CurrCols, 2);
					SimpleGameData.numbHeal = false;
					SimpleGameData.healerEvent = false;
					SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
				} else {
					continue;
				}
			}
			// EVENT TREASURE
			// EVENT TREASURE
			else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				System.out.println(location.x);
				System.out.println(location.y);
				if (action == Action.POINTER_DOWN) {
					if (location.x > 1082 && location.x < 1128 && location.y > 115 && location.y < 197) {
						SimpleGameData.dragSword = true;
					} else if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}
			} else if (action == Action.POINTER_DOWN && SimpleGameData.treasureEvent == true) {
				SimpleGameView.woodenSword(context, SimpleGameData.swordX, SimpleGameData.swordY);
				SimpleGameView.woodenShield(context, SimpleGameData.shieldX, SimpleGameData.shieldY);
				if (location.x > 372 && location.x < 468 && location.y > 105 && location.y < 200) {
					if (action == Action.POINTER_DOWN) {
						SimpleGameData.dragShield = true;
					} else {
						continue;
					}
					SimpleGameData.treasureEvent = false;
				}

			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragShield == true) {
				// Case 1
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(0, 1) == 0
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;
				}
				// Case 2
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(0, 2) == 0
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

					// Case 3
				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(0, 3) == 0
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 4
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(0, 4) == 0
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 5
				if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 0) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0 && SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 6

				if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 1) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0 && SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}
				// Case 7

				if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 2) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0 && SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				}

				// Case 8

				if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208
						&& SimpleGameData.bag.getIndex(1, 3) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0 && SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenShield.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenShield.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragShield = false;

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			} else if (action == Action.POINTER_DOWN && SimpleGameData.dragSword == true) {
				if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 0) == 0 && SimpleGameData.bag.getIndex(1, 0) == 0
						&& SimpleGameData.bag.getIndex(2, 0) == 0) {
					SimpleGameData.bag.changeIndex(0, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 0, SimpleGameData.woodenSword.itemGetId());
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);
					SimpleGameData.dragSword = false;

				}
				if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 1) == 0 && SimpleGameData.bag.getIndex(1, 1) == 0
						&& SimpleGameData.bag.getIndex(2, 1) == 0) {
					SimpleGameData.bag.changeIndex(0, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 1, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 2) == 0 && SimpleGameData.bag.getIndex(1, 2) == 0
						&& SimpleGameData.bag.getIndex(2, 2) == 0) {
					SimpleGameData.bag.changeIndex(0, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 2, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 3) == 0 && SimpleGameData.bag.getIndex(1, 3) == 0
						&& SimpleGameData.bag.getIndex(2, 3) == 0) {
					SimpleGameData.bag.changeIndex(0, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 3, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				}
				if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131
						&& SimpleGameData.bag.getIndex(0, 4) == 0 && SimpleGameData.bag.getIndex(1, 4) == 0
						&& SimpleGameData.bag.getIndex(2, 4) == 0) {
					SimpleGameData.bag.changeIndex(0, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(1, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.bag.changeIndex(2, 4, SimpleGameData.woodenSword.itemGetId());
					SimpleGameData.dragSword = false;
					SimpleGameView.drawBackpack(context, SimpleGameData.bag);

				} else {
					continue;
				}
				System.out.println(SimpleGameData.bag);
				SimpleGameView.drawBackpack(context, SimpleGameData.bag);
			}
			
			else if (action == Action.POINTER_DOWN && SimpleGameData.EnemyEventUp == true) {
				{
					if (SimpleGameData.hero.getLife() > 0 && SimpleGameData.frogWizard.getLifePoint() > 0) {
						if (SimpleGameData.energy > 0) {
							if (action == Action.POINTER_DOWN) {
								//Si le joueur clique sur la case numéro 1 de son sac a dos
								if (location.x > 521 && location.x < 594 && location.y > 59 && location.y < 131) {
									//Si la case est une épée
									if (SimpleGameData.bag.getIndex(0,0) == 1) {
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
									
									//Si la case est de la nourriture
									if (SimpleGameData.bag.getIndex(0, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								}
								//Si le joueur clique sur la case numéro 2
								if (location.x > 601 && location.x < 665 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,1) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);									
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									if (SimpleGameData.bag.getIndex(0, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 3
								if (location.x > 672 && location.x < 735 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,2) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(0, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}	
								//Si le joueur clique sur la case numéro 4
								if (location.x > 749 && location.x < 815 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,3) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(0, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 5
								if (location.x > 826 && location.x < 887 && location.y > 59 && location.y < 131) {
									if (SimpleGameData.bag.getIndex(0,4) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(0, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(0, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 6
								if (location.x > 521 && location.x < 594 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,0) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(1, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 7
								if (location.x > 601 && location.x < 665 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,1) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(1, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 8
								if (location.x > 672 && location.x < 735 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,2) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(1, 2) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 9
								if (location.x > 749 && location.x < 815 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,3) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(1, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 10
								if (location.x > 826 && location.x < 887 && location.y > 140 && location.y < 208) {
									if (SimpleGameData.bag.getIndex(1,4) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(1, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(1, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 11
								if (location.x > 521 && location.x < 594 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,0) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 0) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(2, 0) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 12
								if (location.x > 601 && location.x < 665 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,1) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 1) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(2, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 13
								if (location.x > 672 && location.x < 735 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,2) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 2) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(2, 1) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
								//Si le joueur clique sur la case numéro 14
								if (location.x > 749 && location.x < 815 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,3) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold,SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 3) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(2, 3) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							
								//Si le joueur clique sur la case numéro 15
								if (location.x > 826 && location.x < 887 && location.y > 214 && location.y < 280) {
									if (SimpleGameData.bag.getIndex(2,4) == 1) {
										//Si la case est une épée
										SimpleGameData.frogWizard.receiveDamage(SimpleGameData.woodenSword);
										SimpleGameData.energy -= SimpleGameData.woodenSword.itemGetEnergyCost();
										SimpleGameView.enemyMap(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.frogWizard,
												SimpleGameData.gold, SimpleGameData.energy);
									}
									//Si la case est un shield;
									if (SimpleGameData.bag.getIndex(2, 4) == 2) {
										SimpleGameData.hero.receiveProtection(SimpleGameData.woodenShield.itemGetShield());
										SimpleGameData.energy -= SimpleGameData.woodenShield.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);

									}
									
									if (SimpleGameData.bag.getIndex(2, 4) == 3) {
										SimpleGameData.hero.receiveLife(SimpleGameData.food.itemGetHeal());
										SimpleGameData.energy -= SimpleGameData.food.itemGetEnergyCost();
										SimpleGameView.lifeHero(context,SimpleGameData.hero);
										SimpleGameView.drawEnergy(context, SimpleGameData.energy);
									}
								
								}
							}
						}
						else if(SimpleGameData.energy <= 0){
							
							int randomChoice = SimpleGameData.random.nextInt(2) + 1; 
							if (randomChoice == 1) {
					            System.out.println("L'ennemi attaque !");
					            SimpleGameData.hero.receiveDamage(SimpleGameData.frogWizard);
					            SimpleGameView.lifeHero(context, SimpleGameData.hero);
					        } else if (randomChoice == 2) {
					        	System.out.println("L'ennemi se défend !");
					            SimpleGameData.frogWizard.receiveProtection();
					        }
							
							SimpleGameData.energy = 10;
						}
					}
					
					else if(SimpleGameData.hero.getLife() <=0){
						System.out.println("combat fini");
						SimpleGameView.gameOver(context);
						context.exit(0);
						return;
					}
					else if(SimpleGameData.frogWizard.getLifePoint()<=0) {
						SimpleGameData.gold.increaseGold(5);
						SimpleGameView.setBg(context, SimpleGameData.hero, SimpleGameData.bag, SimpleGameData.gold);
						SimpleGameData.energy = 10;
						SimpleGameData.EnemyEventUp = false;}
				}}
			else if (event.getKey() == KeyboardKey.B && SimpleGameData.MerchantEvent == true) {
				for (int rows = 0; rows<2;rows++) {
					for (int cols = 0; cols < 3; cols++) {
						if (SimpleGameData.bag.getIndex(rows,cols) == 0  && SimpleGameData.cmptMerchant == 0) {
							SimpleGameView.food(context, 1000, rows);
							SimpleGameData.bag.changeIndex(rows, cols, SimpleGameData.food.itemGetId());
							SimpleGameData.gold.decreaseGold(SimpleGameData.food.itemGetPrice());
							System.out.println(SimpleGameData.bag);
							SimpleGameView.drawBackpack(context, SimpleGameData.bag);
							SimpleGameData.cmptMerchant++;
						}
						else {
							continue;
						}
					}
				}
				
			}
			
			
			
			
			
			if (event.getKey() == KeyboardKey.SHIFT) {
				context.exit(0);
				return;
			} else if (event.getKey() != KeyboardKey.SHIFT) {
				continue;
			}
		}
	}

	public static void main(String[] args) {
		Application.run(Color.BLACK, context -> backpackHero(context));

	}
}
