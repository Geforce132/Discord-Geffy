package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.geforce.geffy.main.Utils;

public class CommandSCLinks extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		String mcf = "MCF: <https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1291001>\n";
		String curseForge = "CurseForge: <https://minecraft.curseforge.com/projects/security-craft>\n";
		String pmc = "PlanetMinecraft: <https://www.planetminecraft.com/mod/securitycraft-keypads-retinal-scanners-and-more/>\n";
		String ftb = "FTB: <https://ftb.gamepedia.com/SecurityCraft>\n";
		String github = "Github: <https://github.com/Geforce132/SecurityCraft>\n";
		String forms = "Report form: <https://forms.gle/7LLmGqjeBJ2CTVqq5>";

		Utils.sendMessage(event.getMessage().getChannel().block(), mcf + curseForge + pmc + ftb + github + forms);
	}

	@Override
	public String[] getAliases() {
		return new String[] {"sclinks"};
	}

}
