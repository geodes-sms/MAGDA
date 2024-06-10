# MAGDA – Modeling Assistance with Generative Ai for Domain representation

A tool for domain modeling by class diagrams and GPT-driven recommendations.

## Setting up the environment (Developers):
  * https://www.eclipse.org/downloads/packages/release/juno/sr1/eclipse-modeling-tools
  * While running the experiment, we will have 3 workspaces running, launch the first one, import the folders in the plugin folder using "open projects from file systems"
  * launch a second instance or eclipse application from the first workspace (plugin), make sure you select a new workspace and import in that workspace the editor plugin;
  * put the key of openAI in the file config.properties.
  * launch the third workspace from the second one;
  * Create a new modeling project; import metamodel   



![High-level architectural view of MAGDA](tool-overview.png)
  
          
The high-level overview of the tool is shown in Fig above. We chose the Eclipse platform3 to implement our tool thanks to the rapid development curve it enables through its ready-to-use modeling framework and extensible plug-in–based architecture. The user interacts with the models through the Visual Editor that allows the creation of domain models using UML Class Diagrams. The Modeling Framework  serves for representing and persisting models.
The Recommendation engine  is responsible for querying GPT upon request or edit operations; collecting, organizing, and ranking recommendations; and passing this information to the editor by saving recommendations in the model. Finally, to investigate user behavior during modeling, we developed a Logging facility.

        

        
        

        
 
        
