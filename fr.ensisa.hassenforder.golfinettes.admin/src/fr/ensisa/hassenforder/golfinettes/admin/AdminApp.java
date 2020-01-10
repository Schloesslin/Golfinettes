package fr.ensisa.hassenforder.golfinettes.admin;

import java.util.List;

import javax.swing.SwingUtilities;

import fr.ensisa.hassenforder.golfinettes.admin.model.Event;
import fr.ensisa.hassenforder.golfinettes.admin.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.admin.model.Model;
import fr.ensisa.hassenforder.golfinettes.admin.model.ModelListener;
import fr.ensisa.hassenforder.golfinettes.admin.network.AdminSession;
import fr.ensisa.hassenforder.golfinettes.admin.network.ISession;
import fr.ensisa.hassenforder.golfinettes.admin.ui.Admin_UI;
import fr.ensisa.hassenforder.golfinettes.admin.ui.GUIListener;

/**
 *
 * @author hassenforder
 */
public class AdminApp implements GUIListener {

    private Model model;
    private Admin_UI gui;
    private ModelListener listener;
    private ISession session;

    public AdminApp() {
        model = new Model ();
        gui = new Admin_UI ();
        gui.setListener(this);
        listener = gui;
        session = new AdminSession();
        session.open();
    }

    public Model getModel() {
        return model;
    }

    public Admin_UI getGui() {
        return gui;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	AdminApp golfinette = new AdminApp();
            	golfinette.getGui().setVisible(true);
            }
        });
    }
    
	@Override
	public void onVersionSoftware() {
        new Thread () {
            @Override
            public void run () {
        		String version = session.doSoftwareUpdate(model.getNextSoftwareVersion());
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	gui.notifyStatusChanged("software version update failed");
                        }
                    });
                } else {
            		model.setSoftwareVersion(version);
                	SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                			gui.notifySoftwareVersionChanged(version);
                        	gui.notifyStatusChanged("software version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onVersionUsers() {
        new Thread () {
            @Override
            public void run () {
				String version = session.doUsersUpdate(model.getNextUsersVersion());
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("users version update failed");
                        }
                    });
                } else {
                	model.setUsersVersion(version);
                	SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
							gui.notifyUsersVersionChanged(version);
                    		gui.notifyStatusChanged("users version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onVersionMap() {
        new Thread () {
            @Override
            public void run () {
				String version = session.doMapUpdate(model.getNextMapVersion());
        		if (version == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("map version update failed");
                        }
                    });
                } else {
                	model.setMapVersion(version);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
							gui.notifyMapVersionChanged(version);
                    		gui.notifyStatusChanged("map version updated");
                        }
                    });
                }
            }
        }.start();
	}

	@Override
	public void onGetGolfinetteList() {
        new Thread () {
            @Override
            public void run () {
				List<Golfinette> golfinettes = session.doGetGolfinetteList();
        		if (golfinettes == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("Get golfinettes failed");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	model.setGolfinettes (golfinettes);
                    		gui.notifyStatusChanged("Get golfinettes succeed");
                    		gui.notifyGolfinetteListChanged(golfinettes);
                        }
                    });
                }
            }
        }.start();
	}
	
	@Override
	public void onGetEventList(long id, String kind) {
        new Thread () {
            @Override
            public void run () {
				List<Event> events = session.doGetEventList(id, kind);
        		if (events == null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                    		gui.notifyStatusChanged("Get Event failed");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	model.setEvents (events);
                    		gui.notifyEventListChanged(events);
                    		gui.notifyStatusChanged("Get Event succeed");
                        }
                    });
                }
            }
        }.start();
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
	public void onClearGolfinetteList() {
		model.clearGolfinettes();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		gui.notifyGolfinetteListChanged(model.getGolfinettes());
            }
        });
	}

}
