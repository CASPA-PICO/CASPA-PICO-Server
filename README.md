<h1>CASPA-PICO Server</h1>
<p>Le programme est un projet <a href="https://github.com/gradle/gradle">Gradle</a>, développé en Java version 17.</p>
<h2>Bibliothèques utilisées</h2>
<p>
  <ul>
    <li><a href="https://github.com/spring-projects/spring-boot">Spring Boot</a></li>
    <li><a href="https://github.com/spring-projects/spring-framework">Spring Webflux</a></li>
    <li><a href="https://github.com/spring-projects/spring-framework">Spring Validation</a></li>
    <li><a href="https://github.com/spring-projects/spring-data-mongodb">Spring Data MongoDB</a></li>
    <li><a href="https://github.com/spring-projects/spring-security">Spring Security</a></li>
    <li><a href="https://github.com/thymeleaf">Thymeleaf</a></li>
    <li><a href="https://github.com/influxdata/influxdb-java">InfluxDB Java</a></li>
    <li><a href="https://github.com/JetBrains/kotlin">Kotlin</a></li>
  </ul>
</p>
<h2>Fonctionnement global du programme</h2>
<p>Ce programme sert de serveur web pour le site CASPA-PICO accessible à l'adresse <a href="caspa.icare.univ-lille.fr">caspa.icare.univ-lille.fr</a>.<br/>
Il sert également d'API REST pour la base, s'occupant de l'authentification de la base, de l'activation de la base ainsi que de la mise en ligne des données</p>
<h2>Configuration du programme</h2>
<h3>Configuration du serveur</h3>
<p>
Pour changer la configuration du programme, modifiez le fichier <a href="https://github.com/CASPA-PICO/CASPA-PICO-Server/blob/master/application_dev.properties"><b>application_dev.properties</b></a>.</br>
Plusieurs champs sont modifiables :<br/>
<ul>
  <li><b>server.port</b> : port d'écoute du serveur web</li>
  <li><b>spring.data.mongodb.host</b> : adresse de la base de données MongoDB</li>
  <li><b>spring.data.mongodb.port</b> : port de la base de données MongoDB</li>
  <li><b>spring.data.mongodb.database</b> : nom de la base de données à utiliser</li>
  <li><b>influxdb.token</b> : Token d'authentification pour la base de données InfluxDB</li>
  <li><b>influxdb.bucket</b> : Nom du bucket à utiliser pour enregistrer les données dans la base de données InfluxDB</li>
  <li><b>influxdb.org</b> : Nom de l'organisation à utiliser pour enregistrer les données dans la base de données InfluxDB</li>
  <li><b>influxdb.url</b> : adresse de la base de données InfluxDB</li>
</ul>
<h3>Changement de l'interprétation des données</h3>
<p>
  Pour changer la manière dont les données sont interprétées (convertion du texte brut en points avec des valeurs, un timestamp), vous pouvez modifier la classe <a href="https://github.com/CASPA-PICO/CASPA-PICO-Server/blob/master/src/main/java/fr/polytech/caspapicoserver/dataparser/MV11Parser.java"><b>MV11Parser</b></a>.
</p>
<h2>Compilation du programme</h2>
<p>
  Pour compiler le programme, assurez vous d'avoir installer la version 17 du JDK.
  <h3>Sous Windows</h3>
  <ol>
    <li>Cloner le repo Github et ouvrir un terminal à la racine du projet</li>
    <li>Compiler le programme avec : <i>./gradlew.bat assemble</i></li>
    <li>Le .jar compilé se trouve dans le dossier build/libs/ et est nommé <b>CASPA-PICO-Server-0.0.1-SNAPSHOT.jar</b>
  </ol>
  <h3>Sous Linux</h3>
  <ol>
    <li>Cloner le repo Github et ouvrir un terminal à la racine du projet</li>
    <li>Compiler le programme avec : <i>./gradlew assemble</i></li>
    <li>Le .jar compilé se trouve dans le dossier build/libs/ et est nommé <b>CASPA-PICO-Server-0.0.1-SNAPSHOT.jar</b>
  </ol>
</p>
<h2>Lancement du programme</h2>
<p>
  Regroupez dans un même dossier le fichier .jar et votre fichier de configuration application_dev.properties</br>
  <h3>Pour Windows</h3>
  <ol>
    <li>Ouvrir un terminal dans le dossier contenant le .jar et le .properties</li>
    <li>Lancer le serveur avec la commande :<br/><i>java -jar CASPA-PICO-Server-0.0.1-SNAPSHOT.jar -Dspring.config.location=application_dev.properties</i></li>
  </ol>
  <h3>Pour Linux</h3>
  <ol>
    <li>Ouvrir un terminal dans le dossier contenant le .jar et le .properties</li>
    <li>Lancer le serveur avec la commande :<br/><i>java -jar CASPA-PICO-Server-0.0.1-SNAPSHOT.jar -Dspring.config.location=application_dev.properties</i></li>
  </ol>
</p>
