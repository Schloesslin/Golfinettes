package fr.ensisa.hassenforder.golfinettes.client;

import javax.swing.SwingUtilities;

import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Model;
import fr.ensisa.hassenforder.golfinettes.client.model.ModelListener;
import fr.ensisa.hassenforder.golfinettes.client.model.Version;
import fr.ensisa.hassenforder.golfinettes.client.network.GolfinetteSession;
import fr.ensisa.hassenforder.golfinettes.client.network.ISession;
import fr.ensisa.hassenforder.golfinettes.client.ui.GUIListener;
import fr.ensisa.hassenforder.golfinettes.client.ui.Golfinette_UI;

/**
 *
 * @author hassenforder
 */
public class GolfinetteApp implements GUIListener {

    private Model model;
    private Golfinette_UI gui;
    private ModelListener listener;
    private ISession session;

    public GolfinetteApp() {
        model = new Model ();
        gui = new Golfinette_UI ();
        gui.setListener(this);
        listener = gui;
        session = new GolfinetteSession();
        session.open();
    }

    public Model getModel() {
        return model;
    }

    public Golfinette_UI getGui() {
        return gui;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	GolfinetteApp golfinette = new GolfinetteApp();
            	golfinette.getGui().setVisible(true);
            }
        });
    }
    
	@Override
	public void onDoSoftwareUpdate() {
        new Thread () {
            @Override
            public void run () {
            	String previous = null;
            	if (model.getSoftwareVersion() != null) previous = model.getSoftwareVersion().getVersion();
        		Version version = session.doSoftwareUpdate(previous);
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	gui.notifyStatusChanged("software version update failed");
                        }
                    });
                } else {
                	SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                			model.setSoftwareVersion(version);
                			gui.notifySoftwareVersionChanged(version.getVersion());
                        	gui.notifyStatusChanged("software version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onDoUsersUpdate() {
        new Thread () {
            @Override
            public void run () {
            	String previous = null;
            	if (model.getUsersVersion() != null) previous = model.getUsersVersion().getVersion();
				Version version = session.doUsersUpdate(previous);
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("users version update failed");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
							model.setUsersVersion(version);
							gui.notifyUsersVersionChanged(version.getVersion());
                        	gui.notifyStatusChanged("users version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onDoMapUpdate() {
        new Thread () {
            @Override
            public void run () {
            	String previous = null;
            	if (model.getMapVersion() != null) previous = model.getMapVersion().getVersion();
				Version version = session.doMapUpdate(previous);
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("map version update failed");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
							model.setMapVersion(version);
							gui.notifyMapVersionChanged(version.getVersion());
                        	gui.notifyStatusChanged("map version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onDoSigFox() {
        new Thread () {
            @Override
            public void run () {
				session.doSigFox(model.getLastEvent());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                		gui.notifyStatusChanged("sigfox sent");
                    }
                });
            }
        }.start();
	}

	@Override
	public void onDoWifi() {
        new Thread () {
            @Override
            public void run () {
				boolean result = session.doWifi(model.getEvents());
        		if (! result) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("wifi send failed");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("wifi send succeed");
                        }
                    });
                }
            }
        }.start();
	}
	
	@Override
	public void onVersionSoftware() {
		Version version = model.getSoftwareVersion();
		if (version == null) System.out.println ("Software Version : ---");
		else System.out.println ("Software Version : "+version.toString());
	}

	@Override
	public void onVersionMap() {
		Version version = model.getMapVersion();
		if (version == null) System.out.println ("Map Version : ---");
		else System.out.println ("Map Version : "+version.toString());
	}

	@Override
	public void onVersionUsers() {
		Version version = model.getUsersVersion();
		if (version == null) System.out.println ("Users Version : ---");
		else System.out.println ("Users Version : "+version.toString());
	}

	@Override
	public void onClearEventList() {
		model.clearEvents();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		gui.notifyEventListChanged(model.getEvents());
            }
        });
	}

	@Override
	public void onAddEvent(Event event) {
		model.addEvent(event);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		gui.notifyEventListChanged(model.getEvents());
            }
        });
	}

	@Override
	public void onDeleteEvent(Event event) {
		model.deleteEvent(event);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		gui.notifyEventListChanged(model.getEvents());
            }
        });
	}

	static double centerLatitude = 47.855311;
	static double centerLongitude = 7.557767;
	static double radius = 0.002F;
	static int angle = 0;
	
	@Override
	public void onNextPosition() {
		double radian = 2*Math.PI*angle/360;
		double latitude = centerLatitude + radius + Math.sin(radian);
		double longitude =  centerLongitude + radius + Math.cos(radian);
		angle += 30;
		angle %= 360;
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	gui.notifyPositionChanged((float) latitude, (float) longitude);
            }
        });
	}

	static int temperatureMax = 40;
	static int temperatureMin = -10;
	static int temperatureSlope = 1;
	static int temperature = 20;
	
	static int humidityMax = 70;
	static int humidityMin = 50;
	static int humidity = 65;
	
	@Override
	public void onNextAmbient() {
		if (temperatureSlope == 1) {
			if (temperature == temperatureMax) {
				temperatureSlope = -1;
			} else {
				++temperature;
			}
		} else {
			if (temperature == temperatureMin) {
				temperatureSlope = 1;
			} else {
				--temperature;
			}
		}
		if (humidity == humidityMax) {
			humidity = humidityMin;
		} else {
			++humidity;
		}
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	gui.notifyAmbientChanged(temperature, humidity);
            }
        });
	}

	static int batteryMax = 90;
	static int batteryMin = 70;
	static int batterySlope = -1;
	static int battery = 75;
	
	static int loadMax = 90;
	static int loadMin = 10;
	static int load = 15;

	@Override
	public void onNextPower() {
		if (batterySlope == 1) {
			if (battery == batteryMax) {
				batterySlope = -1;
			} else {
				++battery;
			}
		} else {
			if (battery == batteryMin) {
				batterySlope = 1;
			} else {
				--battery;
			}
		}
		if (load == loadMin) {
			load = loadMax;
		} else {
			--load;
		}
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	gui.notifyPowerChanged(battery, load);
            }
        });
	}

}
