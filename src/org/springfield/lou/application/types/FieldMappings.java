package org.springfield.lou.application.types;

import java.util.HashMap;
import java.util.Map.Entry;

public class FieldMappings {
	
	private static final HashMap<String, String> mappings;
    static
    {
        mappings = new HashMap<String, String>();
        mappings.put("screenshot","screenshot");
        mappings.put("title", "TitleSet_TitleSetInEnglish_title");
        mappings.put("originalTitle", "TitleSet_TitleSetInOriginalLanguage_title");
        mappings.put("provider", "provider");
        mappings.put("year", "SpatioTemporalInformation_TemporalInformation_productionYear");
        mappings.put("language", "originallanguage");
        mappings.put("duration", "TechnicalInformation_itemDuration");
        mappings.put("topic", "topic");
        mappings.put("publisher", "publisherbroadcaster");
        mappings.put("genre", "genre");
        mappings.put("country", "SpatioTemporalInformation_SpatialInformation_CountryofProduction");
        mappings.put("clipTitle", "clipTitle");
        mappings.put("publisher", "publisherbroadcaster");
        mappings.put("broadcastChannel", "firstBroadcastChannel");
        mappings.put("broadcastDate", "SpatioTemporalInformation_TemporalInformation_broadcastDate");
        mappings.put("geographicalCoverage", "SpatioTemporalInformation_SpatialInformation_GeographicalCoverage");
        mappings.put("materialType", "TechnicalInformation_materialType");
        mappings.put("itemType", "recordType");
        mappings.put("itemColour", "TechnicalInformation_itemColor");
        mappings.put("itemSound", "TechnicalInformation_itemSound");
        mappings.put("thesaurusTerms", "ThesaurusTerm");
        mappings.put("extendedDescription", "extendedDescription");
        mappings.put("filename", "filename");
        mappings.put("identifier", "identifier");
        mappings.put("contributors", "contributor");
        mappings.put("aspectRatio", "TechnicalInformation_aspectRatio");
        mappings.put("furtherInformation", "information");
        mappings.put("summaryOriginal", "summary");
        mappings.put("summaryEnglish", "summaryInEnglish");
        mappings.put("originalIdentifier", "originalIdentifier");
        mappings.put("terms", "rightsTermsAndConditions");
        mappings.put("series", "TitleSet_TitleSetInOriginalLanguage_seriesOrCollectionTitle");
        mappings.put("seriesEnglish", "TitleSet_TitleSetInEnglish_seriesOrCollectionTitle");
    }
    
    public static HashMap<String, String> getMappings(){
    	return mappings;
    }
    
    public static String getSystemFieldName(String readable){
    	return mappings.get(readable);
    }
    
    public static String getReadable(String systemName){
    	for (Entry<String, String> entry : mappings.entrySet()) {
            if (systemName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
    	return null;
    }

}
