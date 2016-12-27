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

## Active MQ ##
* Aplikace vyžaduje spuštěný Active MQ
  * Stáhněte a rozbalte Active MQ: ``http://activemq.apache.org/``
  * Spusťte Active MQ
  
    ``bin/activemq start``
        
  * Konzole poběží defaultně na: ``http://localhost:8161/admin/``
    * login: ``admin``
    * heslo: ``admin``
  * Broker URL musí být ponecháno defaultní: ``tcp://localhost:61616``
      
        
  