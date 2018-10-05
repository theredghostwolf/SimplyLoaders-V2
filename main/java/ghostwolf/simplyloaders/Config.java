package ghostwolf.simplyloaders;

import ghostwolf.simplyloaders.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
		
		//categories
	 	private static final String CATEGORY_GENERAL = "general";
	 	private static final String CATEGORY_LOADER = "Item Loader";
	 	
	 	//comments
	 	private static final String rateComment = "Ticks between each operation.";
	 	

	    // This values below you can access elsewhere in your mod:
	 	public static int LoaderTransferRate = 5; // ticks between each operation
	 	public static int LoaderTransferSpeed = 6; // how many item  loader will move at once
	 	
	 	public static int UnloaderTransferRate = 5;
	 	public static int UnloaderTransferSpeed = 6;
	 	
	 	public static int EntityLoaderRate = 10; // ticks between each operation
	 	public static int EntityUnloaderRate = 10; 
	 	
	 	public static int RaintankSize = 1;
	 	public static int RaintankGenerationRate = 10; //ticks between each generation tick
	 	public static int RaintankGenerationSpeed = 250; // amount of MB water generated
	 	
	 	public static int TankcartSize = 24;
	 	
	 	public static int FluidLoaderRate = 5;
	 	public static int FluidLoaderSpeed = 500;
	 	public static int FluidLoaderSize = 24;
	 	
	 	public static int FluidUnloaderRate = 5;
	 	public static int FluidUnloaderSpeed = 500;
	 	public static int FluidUnloaderSize = 24;
	 	
	    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
	    // exist yet and read the values if it does exist.
	    public static void readConfig() {
	        Configuration cfg = CommonProxy.config;
	        try {
	            cfg.load();
	            initGeneralConfig(cfg);
	        } catch (Exception e1) {
	          System.out.println("ERROR LOADING CONFIG");
	        } finally {
	            if (cfg.hasChanged()) {
	                cfg.save();
	            }
	        }
	    }

	    private static void initGeneralConfig(Configuration cfg) {
	        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
	        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
	        LoaderTransferRate = cfg.getInt("LoaderTransferRate", CATEGORY_GENERAL, LoaderTransferRate, 0, 100, rateComment);
	        LoaderTransferSpeed = cfg.getInt("LoaderTransferSpeed", CATEGORY_GENERAL, LoaderTransferSpeed, 0, 64, "Max amount of items moved per operation.");
	    
	        UnloaderTransferRate = cfg.getInt("UnloaderTransferRate", CATEGORY_GENERAL, UnloaderTransferRate, 0, 100, rateComment);
	        UnloaderTransferSpeed = cfg.getInt("UnloaderTransferSpeed", CATEGORY_GENERAL, UnloaderTransferSpeed, 0, 64, "Max amount of items moved per operation. Auto output is double this amount.");
	        
	        EntityLoaderRate = cfg.getInt("EntityLoaderRate", CATEGORY_GENERAL, EntityLoaderRate, 0, 100, rateComment);
	        EntityUnloaderRate = cfg.getInt("EntityUnloaderRate", CATEGORY_GENERAL, EntityUnloaderRate, 0, 100, rateComment);
	   
	        RaintankGenerationRate = cfg.getInt("RaintankGenerationRate", CATEGORY_GENERAL, RaintankGenerationRate, 0, 100, rateComment);
	        RaintankGenerationSpeed = cfg.getInt("RaintankGenerationSpeed", CATEGORY_GENERAL, RaintankGenerationSpeed, 0, 100000, "Amount of MB water generated per tick");
	        RaintankSize = cfg.getInt("RaintankSize", CATEGORY_GENERAL, RaintankSize, 1, 1000, "Amount of buckets the raintank can store.");
	        
	        TankcartSize = cfg.getInt("TankcartSize", CATEGORY_GENERAL, TankcartSize, 1, 1000, "Amount of buckets tankcart can store");
	        
	        FluidLoaderRate = cfg.getInt("FluidLoaderRate", CATEGORY_GENERAL, FluidLoaderRate, 0, 100, rateComment);
	        FluidLoaderSpeed = cfg.getInt("FluidLoaderSpeed", CATEGORY_GENERAL, FluidLoaderSpeed, 0, 100000, "Amount of MB transfered per operation.");
	        FluidLoaderSize = cfg.getInt("FluidLoaderSize", CATEGORY_GENERAL, FluidLoaderSize, 1, 1000, "Amount of buckets fluidloader can store.");
	   
	        FluidUnloaderRate = cfg.getInt("FluidUnloaderRate", CATEGORY_GENERAL, FluidUnloaderRate, 0, 100, rateComment);
	        FluidUnloaderSpeed = cfg.getInt("FluidUnloaderSpeed", CATEGORY_GENERAL, FluidUnloaderSpeed, 0, 100000, "Amount of MB transfered per operation. Auto output is double this amount.");
	        FluidUnloaderSize = cfg.getInt("FluidUnloaderSize", CATEGORY_GENERAL, FluidUnloaderSize, 1, 1000, "Amount of buckets fluidunloader can store.");
	    }
	    
}


