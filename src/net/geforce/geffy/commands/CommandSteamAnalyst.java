package net.geforce.geffy.commands;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.geforce.geffy.commands.sa.SAResourceManager;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class CommandSteamAnalyst extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		if(args.length <= 0) {
			Utils.sendMessage(event.getMessage().getChannel().block(), "You must include a Steam Analyst link!");
			return;
		}
				
		String searchQuery = "steam analyst ";
		
		for(String term : args) {
			searchQuery += (term + " ").replaceAll("null", "");
		}

		try {
			String URL = SAResourceManager.searchSA(searchQuery);
			
			if(URL == null) {
				return;
			}
			
			String site = Jsoup.connect(URL).get().html();

			try (PrintWriter out = new PrintWriter("html.txt")) {
				out.println(site);

				LineNumberReader lnr = new LineNumberReader(new FileReader("html.txt"));

				String imgLink = "";
				
				String oldFnPrice = "";
				String oldMwPrice = "";
				String oldFtPrice = "";
				String oldWwPrice = "";
				String oldBsPrice = "";
				
				String fnPrice = "";
				String mwPrice = "";
				String ftPrice = "";
				String wwPrice = "";
				String bsPrice = "";
				String grade = "";
				
				if(SAResourceManager.getImageLink(URL) != null)
					imgLink = SAResourceManager.getImageLink(URL);
				
				String line = null;
				boolean nextLineContainsImgLink = false;
				
				String title = StringUtils.substringBetween(site, " <title>SteamAnalyst.com - ", " - Counter-Strike: Global Offensive");

				while ((line = lnr.readLine()) != null)
				{
					if(imgLink.isEmpty()) {
						// Ghetto way of obtaining the skin's image url lul
						if(line.contains("<div class=\"item-img\">") && !nextLineContainsImgLink) {
							nextLineContainsImgLink = true;
						}

						if(nextLineContainsImgLink && imgLink.isEmpty()) {
							String imageURL = StringUtils.substringBetween(line, "src=\"", "/\"");

							if(imageURL != null)
								imgLink = imageURL;
						}
					}
					
					if(line.contains("fn list-group-item")) {
						fnPrice = StringUtils.substringBetween(line, "<span class=\"exterior\">Factory New</span><span class=\"price pull-right\">", "<"); 
					}
					
					if(line.contains("mw list-group-item")) {
						mwPrice = StringUtils.substringBetween(line, "<span class=\"exterior\">Minimal Wear</span><span class=\"price pull-right\">", "<"); 
					}
					
					if(line.contains("ft list-group-item")) {
						ftPrice = StringUtils.substringBetween(line, "<span class=\"exterior\">Field-Tested</span><span class=\"price pull-right\">", "<"); 
					}
					
					if(line.contains("ww list-group-item")) {
						wwPrice = StringUtils.substringBetween(line, "<span class=\"exterior\">Well-Worn</span><span class=\"price pull-right\">", "<"); 
					}
					
					if(line.contains("bs list-group-item")) {
						bsPrice = StringUtils.substringBetween(line, "<span class=\"exterior\">Battle-Scarred</span><span class=\"price pull-right\">", "<"); 
					}
					
					if(line.contains("<span class=\"tag d-block\"")) {
						grade = StringUtils.substringBetween(line, ">", " ");
					}
				}

				lnr.close();
				
				SAResourceManager.writeToJson(URL, fnPrice, mwPrice, ftPrice, wwPrice, bsPrice, imgLink);
				
				EmbedBuilder builder = new EmbedBuilder();

				builder.withTitle(title.substring(0, title.indexOf("(") - 1));
				
				builder.withAuthorName("SteamAnalyst");
				builder.withAuthorIcon("https://csgo.steamanalyst.com/images/favicon.png");
				builder.withAuthorUrl(URL);
				builder.withImage(imgLink);
				builder.withColor(SAResourceManager.getEmbedColor(grade));
				
				if(!fnPrice.isEmpty()) builder.appendField("Factory New: ", fnPrice, true);
				if(!mwPrice.isEmpty()) builder.appendField("Minimal Wear: ", mwPrice, true);
				if(!ftPrice.isEmpty()) builder.appendField("Field-Tested: ", ftPrice, true);
				if(!wwPrice.isEmpty()) builder.appendField("Well-Worn: ", wwPrice, true);
				if(!bsPrice.isEmpty()) builder.appendField("Battle-Scarred: ", bsPrice, true);

				builder.withFooterText(SAResourceManager.SEARCH_QUERIES_REMAINING + " searches remaining today");
				
				Utils.sendMessageWithEmbed(event.getChannel(), "", builder.build());
			}
		} catch (IOException e) {
			e.printStackTrace();			
		}

	}

	@Override
	public String[] getAliases() {
		return new String[] {"sa"};
	}

}
