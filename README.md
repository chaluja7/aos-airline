# Semestrální projekt z předmětu AOS #
   
## Build ##
* Aplikaci zbuildíte příkazem 

    ``mvn clean install``
    
* Aplikace podporuje 2 databáze - defaultní postgres a také inmemory hsqldb
* Pro inmemory databázi je nutné aplikaci spustit se Springovým profilem `hsql`

    ``-Dspring.profiles.active=hsql``
    
 * Nutnost nastavit do VM options Tomcata. Pokud chcete využít postgres databázi, není třeba nastavovat nic. Je ale nutné mít lokálně postgres nainstalovaný a mít vytvořenou databázi dle `aos-flight-postgres.properties`

    
## Deploy ##
* Aplikace testována na Tomcat 8.5.6
  