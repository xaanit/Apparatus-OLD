package me.xaanit.apparatus.commands.dev;

import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Basically a clone of https://github.com/ArsenArsen/SelfBot/blob/master/userbot/src/main/java/com/arsenarsen/userbot/command/commands/JavaREPL.java for D4J
 */

public class Eval implements ICommand {

    public static final File WORKING_DIR = new File("Apparatus" + File.separator);

    private static SimpleLogger logger = new SimpleLogger(Eval.class);
    private String part1 = "", part2 = "";


    {
        InputStream p1 = Eval.class.getClassLoader().getResourceAsStream("Pattern.java.part1");
        InputStream p2 = Eval.class.getClassLoader().getResourceAsStream("Pattern.java.part2");
        BufferedReader p1reader = new BufferedReader(new InputStreamReader(p1));
        BufferedReader p2reader = new BufferedReader(new InputStreamReader(p2));
        String line;
        try {
            while ((line = p1reader.readLine()) != null) {
                part1 += line + System.getProperty("line.separator");
            }
        } catch (IOException e) {
            logger.critical("Could not read Part1 of Pattern.java, " + e.getMessage());
        }
        try {
            while ((line = p2reader.readLine()) != null) {
                part2 += line + System.getProperty("line.separator");
            }
        } catch (IOException e) {
            logger.critical("Could not read Part1 of Pattern.java, " + e.getMessage());
        }
    }


    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "evaluate"};
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <expression>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public String getInfo() {
        return "Evaluates the given code.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        IMessage msg = sendMessage(channel, "Eval started.");
        if (args.length > 0) {
            String javaHome = System.getProperty("java.home");
            if (ToolProvider.getSystemJavaCompiler() == null) {
                System.setProperty("java.home", System.getenv().getOrDefault("JDK_HOME", javaHome));
                if (ToolProvider.getSystemJavaCompiler() == null) {
                    System.setProperty("java.home", System.getenv().getOrDefault("JAVA_HOME", javaHome));
                    if (ToolProvider.getSystemJavaCompiler() == null) {
                        EmbedBuilder em = basicEmbed(user, "Eval", CColors.ERROR);
                        em.withDesc("You are missing JDK on your system! Halting..\n\n" +
                                "If you believe this is an error set JDK_HOME and/or JAVA_HOME enviromentals to point to it.\n" +
                                "Better solution would be to add JDK_HOME/bin/ to Path and run this jar using the binary in there!");
                        msg = editMessage(msg, em.build());
                        return;
                    }
                }
            }
            String arg = substringCommand(this, message, true);
            long time = System.currentTimeMillis();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            ExecutorService timer = Executors.newSingleThreadExecutor();
            try {
                EmbedBuilder em = basicEmbed(user, "Eval", CColors.BASIC);
                em.withDesc("Compiling...");
                msg = editMessage(msg, em.build());
                File compileDir = new File(WORKING_DIR, "compile");
                if (!compileDir.exists())
                    compileDir.mkdirs();
                File classStorage = new File(compileDir, "classes" + time);
                File classFile = new File(compileDir, "pkg" + time + File.separator + "Pattern.java");
                classStorage.mkdirs();
                classFile.getParentFile().mkdirs();
                classFile.createNewFile();
                Class<?> compiled = compile(arg, time, errorStream, classFile, classStorage, javaHome);
                final IMessage msg1 = msg;
                Runnable task = () -> {
                    try {
                        Method method = compiled.getDeclaredMethod("execute", IChannel.class, IDiscordClient.class, IGuild.class, IMessage.class);
                        IGuild g = guild;
                        EmbedBuilder em1 = basicEmbed(user, "Eval", CColors.BASIC);

                        em1.withDesc("Output: ```java\n" + method.invoke(null, channel, channel.getClient(), g, msg1).toString() + "```");
                        em1.appendField("Input", "```java\n" + arg + "\n```\n", false);
                        editMessage(msg1, em1.build());
                    } catch (IllegalAccessException | NoSuchMethodException e) {
                        EmbedBuilder em1 = basicEmbed(user, "Eval", CColors.ERROR);
                        em1.withDesc(updateWithException("Input: ```java\n" + arg + "\n```\n" + "Could not execute!\n", e, msg1));
                        editMessage(msg1, em1.build());
                    } catch (InvocationTargetException e) {
                        EmbedBuilder em1 = basicEmbed(user, "Eval", CColors.ERROR);
                        em1.withDesc(updateWithException("Input: ```java\n" + arg + "\n```\n" + "Could not execute!\n", e, msg1));
                        editMessage(msg1, em1.build());
                    }
                };
                Future future = timer.submit(task);
                timer.shutdown();
                future.get(15, TimeUnit.SECONDS);
                if (!timer.isTerminated())
                    timer.shutdownNow();
                delete(classStorage);
                delete(classFile.getParentFile());
            } catch (IOException e) {
                EmbedBuilder em = basicEmbed(user, "Eval", CColors.ERROR);
                em.withDesc(updateWithException("Input: ```java\n" + arg + "\n```\n" + "Compilation failure!", e, msg));
                editMessage(msg, em.build());
            } catch (ClassNotFoundException ignored) {
                EmbedBuilder em = basicEmbed(user, "Eval", CColors.ERROR);
                em.withDesc(updateWithException("Input: ```java\n" + arg + "\n```\n" + "Compilation failure!", ignored, msg));
                editMessage(msg, em.build());
            } catch (InterruptedException | TimeoutException ignored) {
                EmbedBuilder em = basicEmbed(user, "Eval", CColors.ERROR);
                em.withDesc("Input: ```java\n" + arg + "\n```\n" + "Timeout!!");
                editMessage(msg, em.build());
                if (!timer.isTerminated())
                    timer.shutdownNow();
            } catch (ExecutionException e) {
                EmbedBuilder em = basicEmbed(user, "Eval", CColors.ERROR);
                em.withDesc(updateWithException("Input: ```java\n" + arg + "\n```\n" + "Compilation failure!", e, msg));
                editMessage(msg, em.build());
            }
            System.setProperty("java.home", javaHome);
        }

    }

    public static void delete(File toRecurse) throws IOException {
        Files.walk(toRecurse.toPath(), FileVisitOption.FOLLOW_LINKS)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private Class<?> compile(String trim, long time, OutputStream compilerStream, File classFile, File classStorage, String javaHome) throws IOException, ClassNotFoundException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(classFile));
        writer.write("package pkg" + time + ";"
                + System.getProperty("line.separator") + part1 + trim + System.getProperty("line.separator") + part2);
        writer.flush();
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, compilerStream, compilerStream, classFile.getAbsolutePath(), "-d", classStorage.getAbsolutePath());
        System.setProperty("java.home", javaHome);
        URLClassLoader loader = new URLClassLoader(new URL[]{classStorage.toURI().toURL()}, getClass().getClassLoader());
        return loader.loadClass("pkg" + time + ".Pattern");
    }


    public static String updateWithException(String msg, Throwable e, IMessage mesg) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        e.printStackTrace();
        String ctn = msg + "\n```" + sw.toString() + "\n```";
        if (ctn.length() > 2000) {
            ctn = ctn.substring(0, 1995) + "\n```";
        }
        pw.close();
        return ctn;
    }

    public static String substringCommand(ICommand command, IMessage msg, boolean rawContent) {
        String content = rawContent ? msg.getContent() : msg.getFormattedContent();
        return content.substring(command.getName().length()
                + getGuild(msg.getGuild()).getPrefix().length()
                + (content.contains(" ") ? 1 : 0));
    }


}
