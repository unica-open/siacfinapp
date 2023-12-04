/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;

import it.csi.siac.siacfinapp.frontend.ui.exception.ConfigurationException;

/**
 * 
 * @author luca.romanello
 *
 */
public class Normalizzatore implements Serializable, InitializingBean {
	private static final long serialVersionUID = 1L;

	private String[] _modelFields, _translationFields;
	private String modelFields, translationFields, modelClass, outputClass, codificaClass, modelTranslationList, outputTranslatedField, outputField, matchedField, idField;
	private Class<?> _modelClass, _outputClass, _codificaClass;
	private Map<String, PropertyDescriptor> _modelPD, _outputPD, _codificaPD;
	private Map<String, String> others;
	private Map<String, List<String>> _modelOthers = new HashMap<String, List<String>>(0);
	private boolean forceNull, forceEmpty;
	
	public void setModelFields(String modelFields) {
		this.modelFields = modelFields;
		if (modelFields != null) {
			_modelFields = modelFields.split(",");
		}
	}
	public void setTranslationFields(String translationFields) {
		this.translationFields = translationFields;
		if (translationFields != null) {
			_translationFields = translationFields.split(",");
		}
	}
	public String getModelClass() {
		return modelClass;
	}
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}
	public String getOutputClass() {
		return outputClass;
	}
	public void setOutputClass(String outputClass) {
		this.outputClass = outputClass;
	}
	public String getModelTranslationList() {
		return modelTranslationList;
	}
	public void setModelTranslationList(String modelTranslationList) {
		this.modelTranslationList = modelTranslationList;
	}
	public Map<String, String> getOthers() {
		return others;
	}
	public void setOthers(Map<String, String> others) {
		this.others = others;
	}
	public String getOutputTranslatedField() {
		return outputTranslatedField;
	}
	public void setOutputTranslatedField(String outputTranslatedField) {
		this.outputTranslatedField = outputTranslatedField;
	}
	
	public String getMatchedField() {
		return matchedField;
	}
	public void setMatchedField(String matchedField) {
		this.matchedField = matchedField;
	}
	public String getCodificaClass() {
		return codificaClass;
	}
	public void setCodificaClass(String codificaClass) {
		this.codificaClass = codificaClass;
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
	public String getOutputField() {
		return outputField;
	}
	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}
	public boolean isForceNull() {
		return forceNull;
	}
	public void setForceNull(boolean forceNull) {
		this.forceNull = forceNull;
	}
	public boolean isForceEmpty() {
		return forceEmpty;
	}
	public void setForceEmpty(boolean forceEmpty) {
		this.forceEmpty = forceEmpty;
	}
	
	/**
	 * Metodo di comodo per la creazione dei messaggi da rilanciare con la ConfigurationException
	 * 
	 * @param builder
	 * @param messages
	 */
	private void addMessage(StringBuilder builder, Object... messages) {
		if (builder != null && messages != null) {
			for (Object current : messages) {
				builder.append(current);
			}
		}
	}
	
	/**
	 * Metodo di check per verificare la congruenza dei campi indicati con le classi passate o da configurazione (check a deploy time)
	 * o da parametro (check a runtime, se non viene effettuato il primo)
	 * 
	 * @param modelClass
	 * @param outputClass
	 * @param confErrors
	 */
	private void checkClasses(String modelClass, String outputClass, StringBuilder confErrors) {
		BeanInfo _modelInfo = null, _outputInfo = null;
		if (modelClass != null) {
			try {
				_modelClass = Class.forName(modelClass);
				_modelInfo = Introspector.getBeanInfo(_modelClass);
				if (_modelFields != null) {
					outer:
					for (String currentMF : _modelFields) {
						for (PropertyDescriptor currentPD : _modelInfo.getPropertyDescriptors()) {
							if (currentMF.equals(currentPD.getName())) {
								if (_modelPD == null){
									_modelPD = new HashMap<String, PropertyDescriptor>(_modelFields.length);
								}
								_modelPD.put(currentMF, currentPD);
								continue outer;
							}
						}
					}
					if (_modelFields.length != _modelPD.size()){
						addMessage(confErrors, "Introspected field list size;");
					}	
				}
				if (modelTranslationList != null) {
					for (PropertyDescriptor currentPD : _modelInfo.getPropertyDescriptors()) {
						if (modelTranslationList.equals(currentPD.getName())) {
							if (_modelPD == null){
								_modelPD = new HashMap<String, PropertyDescriptor>(1);
							}
							_modelPD.put(modelTranslationList, currentPD);
							break;
						}
					}
					if (!_modelPD.containsKey(modelTranslationList)) addMessage(confErrors, "Introspected translation list;");
				}
				if (others != null && others.size() > 0) {
					for (String currentKO : others.keySet()) {
						outerMF:
						for (String currentMF : _modelFields) {
							for (PropertyDescriptor currentPD : _modelInfo.getPropertyDescriptors()) {
								String key = getKey(currentKO, currentMF);
								if (key.equals(currentPD.getName())) {
									if (_modelPD == null) {
										_modelPD = new HashMap<String, PropertyDescriptor>(1);
									}
									_modelPD.put(key, currentPD);
									List<String> cacheOthers = _modelOthers.get(currentKO);
									if (cacheOthers == null) {
										cacheOthers = new ArrayList<String>(1);
										_modelOthers.put(currentKO, cacheOthers);
									}
									cacheOthers.add(key);
									continue outerMF;
								}
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				addMessage(confErrors, "Model class ", modelClass, " not found;");
			} catch (IntrospectionException ie) {
				addMessage(confErrors, "Model class ", modelClass, " not introspectable;");
			}
		}
		if (outputClass != null) {
			try {
				_outputClass = Class.forName(outputClass);
				_outputInfo = Introspector.getBeanInfo(_outputClass);
				if (outputTranslatedField != null || outputField != null) {
					String[] a = new String[] {outputTranslatedField, outputField};
					outer:
					for (PropertyDescriptor currentPD : _outputInfo.getPropertyDescriptors()) {
						for (String currentF : a) {
							if (currentF != null && currentF.equals(currentPD.getName())) {
								if (_outputPD == null){
									_outputPD = new HashMap<String, PropertyDescriptor>(1);
								}
								_outputPD.put(currentF, currentPD);
								continue outer;
							}
						}
					}
					if (outputTranslatedField != null && !_outputPD.containsKey(outputTranslatedField)) addMessage(confErrors, "Introspected translated output field;");
					if (!_outputPD.containsKey(outputField)) addMessage(confErrors, "Introspected output field;");
				}
				if (others != null && others.size() > 0) {
					outerOF:
					for (String currentVO : others.values()) {
						for (PropertyDescriptor currentPD : _outputInfo.getPropertyDescriptors()) {
							if (currentVO.equals(currentPD.getName())) {
								if (_outputPD == null){
									_outputPD = new HashMap<String, PropertyDescriptor>(others.size());
								}
								_outputPD.put(currentVO, currentPD);
								continue outerOF;
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				addMessage(confErrors, "Output class ", outputClass, " not found;");
			} catch (IntrospectionException ie) {
				addMessage(confErrors, "Output class ", outputClass, " not introspectable;");
			}
		}
	}
	
	/**
	 * Metodo di check per verificare la validita' formale dei parametri di configurazione (check effettuato a deploy time)
	 */
	private void checkConfiguration() {
		StringBuilder confErrors = new StringBuilder();
		BeanInfo _codificaInfo = null;

		if (codificaClass == null) {
			addMessage(confErrors, "Null codifica class;");
		} else {
			try {
				_codificaClass = Class.forName(codificaClass);
				_codificaInfo = Introspector.getBeanInfo(_codificaClass);
			} catch (ClassNotFoundException e) {
				addMessage(confErrors, "Codifica class ", codificaClass, " not found;");
			} catch (IntrospectionException ie) {
				addMessage(confErrors, "Codifica class ", codificaClass, " not introspectable;");
			}
		}
		if (modelFields == null || translationFields == null || _modelFields.length != _translationFields.length) {
			addMessage(confErrors, "Field list size;");
		}
		if (outputField == null) {
			addMessage(confErrors, "Null output field;");
		}
		checkClasses(modelClass, outputClass, confErrors);
		if (idField == null) {
			addMessage(confErrors, "Null id field;");
		}
		if (_codificaInfo != null) {
			String[] a = new String[] {matchedField, idField};
			outer:
			for (PropertyDescriptor currentPD : _codificaInfo.getPropertyDescriptors()) {
				for (String currentF : a) {
					if (currentF != null && currentF.equals(currentPD.getName())) {
						if (_codificaPD == null) {
							_codificaPD = new HashMap<String, PropertyDescriptor>(1);
						}
						_codificaPD.put(currentF, currentPD);
						continue outer;
					}
				}
			}
			// errori di introspezione
			if (matchedField != null && !_codificaPD.containsKey(matchedField)){
				addMessage(confErrors, "Introspected matched field;");
			}
			if (idField != null && !_codificaPD.containsKey(idField)){
				addMessage(confErrors, "Introspected id field;");
			}
		}
		if (confErrors.toString().length() > 0){
			throw new ConfigurationException(confErrors.toString());
		}	
	}
	
	/**
	 * Metodo di check per verificare la congruenza dei parametri passati a runtime
	 * 
	 * @param model
	 * @param prototype
	 */
	private <T> void checkParameters(GenericFinModel model, Class<T> prototype) {
		StringBuilder confErrors = new StringBuilder();
		if (model == null || modelClass != null && !model.getClass().equals(_modelClass)) {
			addMessage(confErrors, "Wrong input parameter;");
		}
		if (prototype == null || outputClass != null && !prototype.equals(_outputClass)) {
			addMessage(confErrors, "Wrong output parameter;");
		}
		if (model != null && prototype != null && _modelClass == null && _outputClass == null) {
			checkClasses(model.getClass().getName(), prototype.getClass().getName(), confErrors);
		}
		if (confErrors.toString().length() > 0){
			throw new ConfigurationException(confErrors.toString());
		}	
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		checkConfiguration();
	}
	
	private String getKey(String prefix, String suffix) {
		return prefix + Character.toUpperCase(suffix.charAt(0)) + suffix.substring(1);
	}
	
	/**
	 * Metodo che effettua la vera e propria normalizzazione dei dati presenti in model producendo una lista di oggetti di tipo prototype
	 * 
	 * @param model
	 * @param prototype
	 * @return
	 */
	public <T> List<T> getModelNormalizzato(GenericFinModel model, Class<T> prototype) {
		List<T> result = new ArrayList<T>(0);
		checkParameters(model, prototype);
		List<?> mtl = null;
		for (int i = 0; i < _modelFields.length; i++) {
			String currentMF = _modelFields[i];
			String currentTF = _translationFields[i];
			PropertyDescriptor pdMF = _modelPD.get(currentMF);
			PropertyDescriptor pdTF = _outputPD.get(outputTranslatedField);
			PropertyDescriptor pdF = _outputPD.get(outputField);
			try {
				T currentT = prototype.newInstance();
				Object value = pdMF.getReadMethod().invoke(model);
				if (value != null && String.valueOf(value).length() > 0 || value == null && forceNull || forceEmpty) {
					pdF.getWriteMethod().invoke(currentT, getValue(currentMF, value));
					if (modelTranslationList != null) {
						if (mtl == null) {
							PropertyDescriptor pdMTL = _modelPD.get(modelTranslationList);
							mtl = (List<?>)pdMTL.getReadMethod().invoke(model);
						}
						PropertyDescriptor pdMFL = _codificaPD.get(matchedField);
						PropertyDescriptor pdIFL = _codificaPD.get(idField);
						for (Object o : mtl) {
							Object currentMatched = pdMFL.getReadMethod().invoke(o);
							if (currentTF.equals(currentMatched)) {
								Object currentId = pdIFL.getReadMethod().invoke(o);
								pdTF.getWriteMethod().invoke(currentT, getTranslatedValue(currentMF, currentId));
								break;
							}
						}
					} else {
						pdTF.getWriteMethod().invoke(currentT, getTranslatedValue(currentMF, currentTF));
					}
					for (Entry<String, String> currentE : others.entrySet()) {
						String key = getKey(currentE.getKey(), currentMF);
						if (_modelPD.containsKey(key)) {
							PropertyDescriptor pdMO = _modelPD.get(key);
							PropertyDescriptor pdOO = _outputPD.get(currentE.getValue());
							Object valueO = pdMO.getReadMethod().invoke(model);
							pdOO.getWriteMethod().invoke(currentT, getValue(currentE.getKey(), valueO));
						}
					}
					result.add(currentT);
				}
			} catch (Exception exc) {
//				eccezione
			}
		}
		return result;
	}
	
	
	/**
	 * Metodo placeholder per garantire future implementazioni custom nella valorizzazione del parametro normalizzato
	 * 
	 * @param field
	 * @param v
	 * @return
	 */
	protected Object getValue(String field, Object v) {
		return v;
	}
	
	/**
	 * Metodo placeholder per garantire future implementazioni custom nella valorizzazione del parametro tradotto
	 * 
	 * @param field
	 * @param tv
	 * @return
	 */
	protected Object getTranslatedValue(String field, Object tv) {
		return tv;
	}
}
