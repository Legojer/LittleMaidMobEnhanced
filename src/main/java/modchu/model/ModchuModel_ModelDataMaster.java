package modchu.model;import java.util.Date;import java.util.Map;import java.util.concurrent.ConcurrentHashMap;import littleMaidMobX.models.IModelCaps;import modchu.lib.Modchu_Debug;import modchu.lib.Modchu_Reflect;import modchu.lib.characteristic.Modchu_IEntityCapsBase;public class ModchuModel_ModelDataMaster {	public static ModchuModel_ModelDataMaster instance = new ModchuModel_ModelDataMaster();	protected ConcurrentHashMap<Object, ModchuModel_ModelDataBase> dataMap = new ConcurrentHashMap();	protected ConcurrentHashMap dataLMMCapsMap = new ConcurrentHashMap();	public boolean resetFlag = false;	public Date timeDate;	public ModchuModel_ModelDataMaster() {	}	public ModchuModel_ModelDataBase getPlayerData(Object o) {		if (o == null) return null;		Object entity = null;		ModchuModel_ModelDataBase data = null;		if (o instanceof IModelCaps) {			Map<String, Integer> caps = ((IModelCaps)o).getModelCaps();			entity = ((IModelCaps)o).getCapsValue(caps.get("Entity"), null);			if (entity != null)				data = dataMap.get(entity);					} else data = dataMap.get(o);		boolean b = false;		if (data != null) {			int initFlag = data.getCapsValueInt(data.caps_initFlag);			if (initFlag < 2) {				Modchu_Debug.lDebug("ModchuModel_ModelDataMaster getPlayerData dataMap.get caps_initFlag < 2 initFlag="+initFlag);				initFlag++;				data.setCapsValue(data.caps_initFlag, initFlag);				b = true;			}		} else {			//Modchu_Debug.lDebug("ModchuModel_ModelDataMaster getPlayerData dataMap.get data == null");			b = true;		}		if (b				| resetFlag) {			if (resetFlag) {				resetFlag = false;				clearDataMap();				data = null;			}			data = newPlayerData(o, data);			if (data != null) {				//Modchu_Debug.mDebug("ModchuModel_ModelDataMaster getPlayerData dataMap.put o="+o);				if (data.getCapsValueInt(data.caps_initFlag) == 1) data.setCapsValue(data.caps_initFlag, 2);				if (entity != null) {					dataMap.put(entity, data);				} else {					dataMap.put(o, data);				}			}		}		return data;	}	public ModchuModel_ModelDataBase newPlayerData(Object o) {		return newPlayerData(o, null);	}	public ModchuModel_ModelDataBase newPlayerData(Object o, ModchuModel_ModelDataBase data) {		if (o != null); else return null;		if (data != null); else data = new ModchuModel_ModelDataBase();		if (o instanceof IModelCaps) {			data.setCapsValue(data.caps_subEntityCaps, o);			//Modchu_Debug.mDebug("ModchuModel_ModelDataMaster newPlayerData set o");		} else if (Modchu_Reflect.loadClass("EntityLivingBase").isInstance(o)) {			data.setCapsValue(data.caps_Entity, o);		} else {			Modchu_Debug.lDebug1("ModchuModel_ModelDataMaster newPlayerData !MMM_IModelCaps && !isEntityLivingBase o="+o);		}		//Modchu_Debug.mDebug("ModchuModel_ModelDataMaster newPlayerData end");		data = newPlayerDataDefaultSetting(o, data);		data.setCapsValue(data.caps_initFlag, 2);		return data;	}	public ModchuModel_ModelDataBase newPlayerDataDefaultSetting(Object o, ModchuModel_ModelDataBase data) {		data.setCapsValue(data.caps_freeVariable, "showArmor", true);		data.setCapsValue(data.caps_localFlag, true);		return data;	}	public float getActionSpeed(ModchuModel_ModelDataBase modelData) {/*    	World theWorld = Modchu_Main.getTheWorld();    	float f = (float)(theWorld.getWorldTime() - modelData.getCapsValueInt(modelData.caps_actionTime));    	modelData.setCapsValue(modelData.caps_actionTime, (int) theWorld.getWorldTime());*/    	timeDate = new Date();    	float f = (float)((int) timeDate.getTime() - modelData.getCapsValueInt(modelData.caps_actionTime));    	//Modchu_Debug.mDebug("getActionSpeed f="+f);    	modelData.setCapsValue(modelData.caps_actionTime, (int) timeDate.getTime());    	//Modchu_Debug.mDebug("getActionSpeed (int) timeDate.getTime()="+(int) timeDate.getTime()+" f="+f);    	return f;    }	public void clearDataMap() {		dataMap.clear();	}	public void removeDataMap(Object o) {		dataMap.remove(o);	}	public Object getLMMCaps(Object o) {		if (dataLMMCapsMap.containsKey(o)) return dataLMMCapsMap.get(o);		Modchu_IEntityCapsBase entityCaps = o instanceof Modchu_IEntityCapsBase ? (Modchu_IEntityCapsBase) o : null;		if (entityCaps != null); else return o;		Object caps = Modchu_Reflect.newInstance("modchu.lib.lmm.characteristic.Modchu_LMMEntityCaps", new Class[]{ ModchuModel_ModelDataBase.class, Modchu_Reflect.loadClass("EntityLivingBase") }, new Object[]{ entityCaps, entityCaps.getCapsValue(entityCaps.caps_Entity) });		dataLMMCapsMap.put(entityCaps, caps);		return caps;	}}