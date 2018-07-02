/**
 * Event package for all Minecraft process IO events. A majority of these events are fired by {@link
 * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService} after a {@link
 * info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftConsoleOutputEvent} instance is cleaned by {@link
 * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputCleanerService}, then parsed by {@link
 * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService} into various events.
 */
package info.addisoncrump.oklahoma.bot.minecraft.event;