package it.itpao25.NMSReport.gui;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class itemGUI {
	
	public String material;
	public String id;
	public String id_data = null;
	public int Posizione;
	public int Quantita;
	public String nome;
	public ItemStack Item;
	public int dataItem;
	public List<String> lore;
	private String perms;
    
	@SuppressWarnings("deprecation")
	public itemGUI(String nomeLocalItem, Player nomePlayer, Inventory inv, OfflinePlayer target) {
		
		
		// Per prima cosa, controllo se l'item ha un permesso personalizzato
		if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".permission" ) != null ) {
			this.perms = ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".permission" );
			if( Main.debug ) {
				new ConsoleMessage("Check player has permission custom " + this.perms +" for iteme " + nomeLocalItem);
			}
			if(PermissionUtil._has(nomePlayer, this.perms, true) == false) {
				if( Main.debug ) {
					new ConsoleMessage("The player " + nomePlayer.getName() + " does not have a permission");
				}
				return;
			}
		}
		
		// Poi controllo se l'item selezionato può essere usato per le segnalazioni generali
		if(target == null) {
			if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".report-general" ) != null ) {
				if ( ReporterGUIC.getBoolean( "gui.item."+ nomeLocalItem +".report-general" ) != true ) {
					return;
				}
			}
		} else {
			if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".report-general" ) != null ) {
				if ( ReporterGUIC.getBoolean( "gui.item."+ nomeLocalItem +".report-general" ) == true ) {
					return;
				}
			}
		}
		
		//Controllo l'id
		if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".itemtype" ) != null ) {
			
			this.material = ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".itemtype" ).toUpperCase();
		} else 
		if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".id" ) != null) {
			// Prendo l'id dell'item corrent
			this.id = ReporterGUIC.getString("gui.item."+ nomeLocalItem +".id");
			if(this.id.contains(":")) {
				String[] split = this.id.split(":");
				this.id = split[0];
				this.id_data = split[1]; 
			}
		} else {
			
			// Messaggio che dice di impostare o un nome o un item
			new ConsoleMessage("The itemtype or the id of the item '"+ nomeLocalItem +"' is not present or is not valid");
			this.material = "STONE";
		}
		
		// Controllo la quantità degli oggetti
		if ( ReporterGUIC.getInt( "gui.item."+ nomeLocalItem +".amout" ) != 0 ) {
			
			this.Quantita = ReporterGUIC.getInt("gui.item."+ nomeLocalItem +".amout");	
		} else {
			this.Quantita = 1;
		}
		
		//Posizione dell'item
		this.Posizione = ReporterGUIC.getInt("gui.item."+ nomeLocalItem +".slot");

		
		//Controllo il nome 
		if ( ReporterGUIC.getString("gui.item."+ nomeLocalItem +".name") != null ) {
			
			this.nome = ReporterGUIC.getString("gui.item."+ nomeLocalItem +".name");
		} else {
			
			Main.getInstance().getServer().getLogger().info("[ReporterGUI Error] The name of the item '"+ nomeLocalItem +"' is not present or is invalid");
			this.nome = "Name not found";
		}
		
		/* Controllo se il valore inserito è
		 * itemtype oppure è un ID  */
		
		if ( this.id != null ) {
			if(this.id_data != null) {
				this.Item = new ItemStack(Material.getMaterial(Integer.parseInt(this.id)), this.Quantita, (short) Integer.parseInt(this.id_data));
			} else {
				this.Item = new ItemStack(Material.getMaterial(Integer.parseInt(this.id)), this.Quantita);
			}
		} else {
			this.Item = new ItemStack(Material.getMaterial(this.material),this.Quantita);
		}
		
		/* Imposta il nome dell'oggetto */
		
		ItemMeta imItemGUI = this.Item.getItemMeta();
		imItemGUI.setDisplayName(Utili.color(nomePlayer, this.nome));
		
		
		/* Controllo il lore */
		if( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".lore" ) != null ) {
			
			this.lore = ReporterGUIC.get().getStringList("gui.item."+ nomeLocalItem +".lore");
			
			for ( int c = 0; c <= this.lore.size()-1; c++)
			{
				this.lore.set(c,Utili.color(nomePlayer, this.lore.get(c)));
			}
					
			imItemGUI.setLore(this.lore);    
		} 
		
		// Gestione dell'enchanting
		if ( ReporterGUIC.getString( "gui.item."+ nomeLocalItem +".enchant" ) != null ) 
		{
			String EnchantItem = ReporterGUIC.getString("gui.item."+ nomeLocalItem +".enchant").toUpperCase();
			if(Utili.isContainComma(EnchantItem)) 
			{
				String[] EnchantItemString = EnchantItem.split(",");
				if(Main.debug == true) {
					nomePlayer.sendMessage("The enchants name of item "+ nomeLocalItem +" is: " + EnchantItemString[0]);
					nomePlayer.sendMessage("The enchants level of item "+ nomeLocalItem +" is: " + EnchantItemString[1]);
				}
				Enchantment EnchantmentNameItem = Enchantment.getByName(EnchantItemString[0]);
				
				int numeroEnchant = Integer.parseInt((EnchantItemString[1].trim()));
				imItemGUI.addEnchant(EnchantmentNameItem, numeroEnchant, true);
			} else {
				/* Non è stato specificato il livello dell'enchant */
				Enchantment EnchantmentNameItem = Enchantment.getByName(EnchantItem);
				imItemGUI.addEnchant(EnchantmentNameItem, 1, true);
			}
		}
		
		// Chiudo i meta dell'item
		this.Item.setItemMeta(imItemGUI);
		
		if(Main.debug == true) {
			String NomeItem = ""+ this.Item;  
			nomePlayer.sendMessage(NomeItem);
		}
		
		if(inv != null) {
			if(Main.debug) {
				System.out.println("Posiziono item :"+ this.id);
			}
			inv.setItem(this.Posizione, this.Item);
		}
	}
}
