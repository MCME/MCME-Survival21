package com.mcmiddleearth.mcmesurvival21.outpost;

import com.mcmiddleearth.mcmesurvival21.MCMESurvival21;
import com.mcmiddleearth.mcmesurvival21.team.Team;
import com.mcmiddleearth.mcmesurvival21.team.TeamManager;
import com.mcmiddleearth.pluginutil.plotStoring.IStoragePlot;
import com.mcmiddleearth.pluginutil.plotStoring.InvalidRestoreDataException;
import com.mcmiddleearth.pluginutil.plotStoring.MCMEPlotFormat;
import com.mcmiddleearth.pluginutil.plotStoring.StoragePlotSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Outpost implements IStoragePlot {

    private final String name;
    private String owner;

    private final Location location;
    private final int chunkX, chunkZ;

    private Map<String,byte[]> terrain = new HashMap<>();

    private final static int radius = 5;
    private final static int radiusSquared = radius * radius;

    private final static int updateInterval = 25;  //ticks

    private final static PotionEffect outpostEffect = new PotionEffect(PotionEffectType.REGENERATION, updateInterval,1,true,true,true);

    private final static File outpostFolder = new File(MCMESurvival21.getPlugin(MCMESurvival21.class).getDataFolder(),"outposts");
    private final File file;
    private final static String fileExtension = ".out";

    static {
        if(!outpostFolder.exists()) {
            outpostFolder.mkdirs();
        }
    }

    public Outpost(Location location) {
        name = ""+System.currentTimeMillis();
        this.location = location;
        setBlocks();
        chunkX = location.getChunk().getX();
        chunkZ = location.getChunk().getZ();
        file = new File(outpostFolder,name+fileExtension);
        owner = "neutral";
        new BukkitRunnable() {
            public void run() {
                saveTerrain("neutral");
            }
        }.runTaskLater(MCMESurvival21.getPlugin(MCMESurvival21.class),1);
    }

    public File getFile() { return file; }

    public Location getLocation() {
        return location;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public static int getUpdateInterval() {
        return updateInterval;
    }

    public static int getRadiusSquared() {
        return radiusSquared;
    }

    public static File getOutpostFolder() {
        return outpostFolder;
    }

    public void checkOwner() {
        if(getLocation().getWorld().isChunkLoaded(getChunkX(),getChunkZ())) {
            Collection<Player> playersInside = getLocation().getWorld().getNearbyPlayers(getLocation(), radius,
                    player -> getLocation().distanceSquared(player.getLocation())<radiusSquared);
            if(!playersInside.isEmpty()) {
                Iterator<Player> iterator = playersInside.iterator();
                Team firstTeam = TeamManager.getTeam(iterator.next());
                while(iterator.hasNext() && firstTeam == null) {
                    firstTeam = TeamManager.getTeam(iterator.next());
                }
                while(iterator.hasNext()) {
                    Team compareTeam = TeamManager.getTeam(iterator.next());
                    if(compareTeam != null && compareTeam != firstTeam) {
                        setOwner(null, playersInside);
                        return;
                    }
                }
                setOwner(firstTeam, playersInside);
                return;
            }
            setOwner(null, new ArrayList<>());
        }
    }

    private void setBlocks() {
        location.getBlock().setType(Material.BEACON);
        location.getBlock().getRelative(BlockFace.UP).setType(Material.WHITE_STAINED_GLASS);
        for(int i = -1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
                location.getBlock().getRelative(i,-1,j).setType(Material.IRON_BLOCK);
            }
        }
    }

    private void setOwner(Team team, Collection<Player> inside) {
        if(!(location.getBlock().getState() instanceof Beacon)) {
            //location.getBlock().setType(Material.BEACON);
            setBlocks();
        }
        String newOwner = (team!=null?team.getName():"neutral");
        location.getBlock().getRelative(BlockFace.UP).setType((team==null?Material.WHITE_STAINED_GLASS:team.getBeaconMaterial()));
        if(!newOwner.equals(owner)) {
            owner = newOwner;
            pasteTerrain(owner);
        }
        inside.forEach(player -> {
            if(team != null && team.getMembers().contains(player.getUniqueId())) {
                player.addPotionEffect(outpostEffect);
            }
        });
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public Location getLowCorner() {
        return location.clone().add(-radius,-radius,-radius);
    }

    @Override
    public Location getHighCorner() {
        return location.clone().add(radius,radius,radius);
    }

    @Override
    public boolean isInside(Location location) {
        return this.location.getWorld().equals(location.getWorld())
                && this.location.getBlockX()-radius < location.getBlockX() && this.location.getBlockX()+radius > location.getBlockX()
                && this.location.getBlockY()-radius < location.getBlockY() && this.location.getBlockY()+radius > location.getBlockY()
                && this.location.getBlockZ()-radius < location.getBlockZ() && this.location.getBlockZ()+radius > location.getBlockZ();
    }

    public void saveTerrain(String team) {
        byte[] nbtData = null;
        try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream outStream = new DataOutputStream(
                    new BufferedOutputStream(
                            new GZIPOutputStream(
                                    byteOut)))) {
            new MCMEPlotFormat().save(this, outStream, new StoragePlotSnapshot(this));
            outStream.flush();
            outStream.close();
            nbtData = byteOut.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        if(nbtData!=null) {
            terrain.put(team, nbtData);
        }
        saveToFile();
    }

    private void pasteTerrain(String team) {
        byte[] nbtData = terrain.get(team);
        if(nbtData!=null) {
            try (DataInputStream in = new DataInputStream(
                    new BufferedInputStream(
                            new GZIPInputStream(
                                    new ByteArrayInputStream(nbtData))))) {
                new MCMEPlotFormat().load(this, in);
            } catch (IOException | InvalidRestoreDataException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveToFile() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(file.exists()) {
                    file.delete();
                }
                try(DataOutputStream outStream = new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(file)))) {
                    writeLocation(outStream,location);
                    outStream.writeInt(terrain.size());
                    for(Map.Entry<String,byte[]> terrainData: terrain.entrySet()) {
                        outStream.writeUTF(terrainData.getKey());
                        byte[] nbtData = terrainData.getValue();
                        outStream.writeInt(nbtData.length);
                        int i = 0;
                        final int inc = 4048;
                        while (i < nbtData.length) {
                            outStream.write(nbtData, i, Math.min(inc, nbtData.length - i));
                            i += inc;
                        }
                    }
                    outStream.flush();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskAsynchronously(MCMESurvival21.getPlugin(MCMESurvival21.class));
    }

    private void writeLocation(DataOutputStream out, Location loc) throws IOException {
        out.writeInt(loc.getBlockX());
        out.writeInt(loc.getBlockY());
        out.writeInt(loc.getBlockZ());
    }

    public Outpost(File file) throws IOException {
        try(DataInputStream inStream = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(file)))
        ) {
            this.file = file;
            this.name = file.getName().substring(0,file.getName().lastIndexOf('.'));
            location = readLocation(inStream);
            chunkX = location.getChunk().getX();
            chunkZ = location.getChunk().getZ();
            terrain = new HashMap<>();
            int terrainLength = inStream.readInt();
            for(int i = 0; i<terrainLength; i++) {
                String team = inStream.readUTF();
                int terrainSize = inStream.readInt();
                final int inc = 4048;
                int read;
                int left = terrainSize;
                byte[] buffer = new byte[inc];
                try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
                    do {
                        read = inStream.read(buffer, 0, Math.min(inc, left));
                        if (read > 0) {
                            byteOut.write(buffer, 0, read);
                        }
                        left -= read;
                    } while (left > 0);
                    terrain.put(team, byteOut.toByteArray());
                }
            }
            location.getBlock().setType(Material.BEACON);
            location.getBlock().getRelative(BlockFace.UP).setType(Material.WHITE_STAINED_GLASS);
        }
    }

    private Location readLocation(DataInputStream in) throws IOException {
        return new Location (Bukkit.getWorlds().get(0),
                in.readInt(),in.readInt(),in.readInt());
    }


}
