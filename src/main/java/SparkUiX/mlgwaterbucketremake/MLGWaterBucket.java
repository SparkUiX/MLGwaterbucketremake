package SparkUiX.mlgwaterbucketremake;

//This Mod is a high version remake of the MLG Water Bucket https://github.com/Tfarcenim/MLGWaterBucket/

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
//import net.minecraft.util.math.AABB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
//import net.neoforged.common.ForgeConfigSpec;
//import net.neoforged.common.MinecraftForge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

//import tfar.mlgwaterbucket.mixin.MinecraftMixin;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MLGWaterBucket.MODID)
public class MLGWaterBucket
{
    // Directly reference a log4j logger.

    public static final String MODID = "mlgwaterbucketremake";

    public MLGWaterBucket() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
//        NeoForge.EVENT_BUS.addListener(this::doClientStuff);
    }

//    public static final ItemStack water = Items.WATER_BUCKET.getDefaultInstance();

    public static boolean PickInWater=true;
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class MlgMod {
////        @SubscribeEvent
//        @SubscribeEvent
//        public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
//                    CustomLoop.start();
//        }
//
//        @SubscribeEvent
//        public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
//            CustomLoop.stop();
//        }
        @SubscribeEvent
        public static void doClientStuff(PlayerTickEvent.Post event) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
//            checkNearbyWaterAndGetLookAngles(4.4);
//            System.out.printf("Playerssssssss: %s\n", player.getName().getString());
            if (!PickInWater&&player.isInWater()&&Math.abs(player.getDeltaMovement().y+0.005)<=0.001) {
//            ((MinecraftMixin) Minecraft.getInstance()).$rightClickMouse();
                LookAngles lkat=checkNearbyWaterAndGetLookAngles(4.4);

                if(lkat.yaw!= -1 && lkat.pitch != -1&&player.getMainHandItem().getItem() == Items.BUCKET) {
                    // 计算玩家的朝向
                    placeWater(lkat.yaw, lkat.pitch);
                    PickInWater = true;
                    System.out.printf("Pickup waterSHOUSHUI\n");
                }
                //收水
//
            }
            if (player.isFallFlying() || player.isInWater() || player.getDeltaMovement().y > -.4)
                return;
//            else
//                PickInWater=true;
            if(player.getDeltaMovement().y<-.4)
                PickInWater= true;

//            double ground = Utils.distanceToGround(-player.getDeltaMovement().y);
            double ground =(player.getEyeY()-Minecraft.getInstance().level.clip(new ClipContext(
                    player.getEyePosition(1.0f),
                    player.getEyePosition(1.0f).add(0,-64,0),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            )).getBlockPos().getY());
//        int threshold = ClientConfig.threshold.get();
//            System.out.printf("PlayergetDeltaMovement: %s\n", player.getDeltaMovement().y);
            double fallDist = player.fallDistance;

            if (fallDist < 4) return;
            if(fallDist >= 4) PickInWater=false;

            double eta = -ground/player.getDeltaMovement().y;
//            System.out.printf("eta:"+(ground/- player.getDeltaMovement().y +"\n"));
//            System.out.printf("eta: %s\n", eta);
//            System.out.printf("ground: %s\n", ground);
            //if there is more than 6 ticks to impact, do nothing
//            if (ground-player.getEyeHeight() > 4) {
//                return;
//            }


            //5 to 8 ticks before impact
//        if (eta > 5) {
//            //if already in hotbar, skip
//            for (int i = 0; i < 9;i++) {
//                if (isSupportedItem(player.inventory.mainInventory.get(i))) {
//                    return;
//                }
//            }
//
//            //attempt a shift click
//            for (int i = 9; i < 36;i++) {
//                if (isSupportedItem(player.inventory.mainInventory.get(i))) {
//                    PlayerController playerController = Minecraft.getInstance().playerController;
//                    Container container = player.container;
//                    playerController.windowClick(container.windowId,i,0, ClickType.QUICK_MOVE,player);
//                    Minecraft.getInstance().getConnection().sendPacket(new CCloseWindowPacket(container.windowId));
//                    return;
//                }
//            }
//
//        }

            //2.5 to 5 ticks
            if (eta < 10||ground<10) {
                changeHeldSlot(findWaterBucket(player));


//                System.out.printf("ChangeHeldSlot: %d\n", findWaterBucket(player));
//            if (player.getInventory().(net.minecraft.world.item.Items.WATER_BUCKET)) {
//                if (holdingSupportedItem(player)) {
//                    return;
//                } else {
//                    //hotbar is fast
//                    for (int i = 0; i < 9;i++) {
//                        if (isSupportedItem(player.getInventory().mainInventory.get(i))) {
//                            player.inventory.currentItem = i;
//                            return;
//                        }
//                    }
//                }
//            } else {
//                System.out.println("this might hurt");
//            }

            }

//        //1.5 to 2 ticks before impact  (eta <2||ground<4.5)
//            System.out.printf("Try to place water"+PickInWater);
        if (isBlockReachableAtYawPitch(player,180,90,4.4)&&player.getMainHandItem().getItem() == Items.WATER_BUCKET) {
//            player.rotationPitch = 90;


            placeWater(180,90);
            PickInWater = false;
//            System.out.printf("Place waterFFFFFSSSSSS\n");
        }

        }

//    public static boolean holdingSupportedItem(PlayerEntity player) {
//        return isSupportedItem(player.getHeldItemMainhand()) || isSupportedItem(player.getHeldItemOffhand());
//    }

        public static int findWaterBucket(@NotNull Player player) {
            Inventory inventory = player.getInventory();
            int waterBucketSlot = -1;

            for (int i = 0; i < inventory.items.size(); i++) {
                ItemStack stack = inventory.items.get(i);
                if (stack.getItem() == Items.WATER_BUCKET) {
                    waterBucketSlot = i;
                    break;
                }
            }

            return waterBucketSlot;

        }

        public static void changeHeldSlot(int SlotB) {//SoltB是水桶的槽位
            if(SlotB < 0) return; // 防止槽位小于0的情况
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            Inventory inventory = player.getInventory();
//        mc.getConnection().send(new ServerboundSetCarriedItemPacket(slot));
            int SlotA = mc.player.getInventory().selected;
            if (SlotB > 8)
                    Minecraft.getInstance().gameMode.handleInventoryMouseClick(
                            player.containerMenu.containerId,
                            SlotB,
                            SlotA,
                            net.minecraft.world.inventory.ClickType.SWAP,
                            player
                    );
                 else
                    inventory.selected = SlotB;
//        System.out.printf("Select: %d\n", slot);
//

        }
        public static boolean isBlockReachableAtYawPitch(Player player, float yawDegrees, float pitchDegrees, double reachDistance) {
            // 将角度转换为弧度
            double yawRad = Math.toRadians(-yawDegrees);
            double pitchRad = Math.toRadians(-pitchDegrees);

            // 计算方向向量
            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);
            Vec3 direction = new Vec3(x, y, z).normalize();

            // 获取玩家眼睛位置
            Vec3 eyePos = player.getEyePosition(1.0f);
            Vec3 endPos = eyePos.add(direction.scale(reachDistance));

            // 创建射线检测上下文
            ClipContext context = new ClipContext(
                    eyePos,
                    endPos,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            );

            // 执行射线检测
            HitResult result = player.level().clip(context);

            // 判断是否命中方块
            return result.getType() == HitResult.Type.BLOCK;
        }

        public static void placeWater(float x,float y) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.getConnection() == null || mc.player == null) return;

            // 获取下一个动作序列号，以匹配客户端多玩家模式要求
//        int seq = mc.getConnection().
//         构造并发送放水包：手持 MAIN_HAND，在 hitResult 指示的位置放置水
            mc.getConnection().send(
                    new ServerboundUseItemPacket(
                            InteractionHand.MAIN_HAND,
                            0,
                            x,
                            y

                    )
            );
        }
        public record LookAngles(float yaw, float pitch) {}

        public static LookAngles checkNearbyWaterAndGetLookAngles(double radius) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return new LookAngles(0, 90);

            Vec3 eyePos = mc.player.getEyePosition(1.0F);
            BlockPos center = BlockPos.containing(eyePos);

            BlockPos nearestWater = null;
            double nearestDistanceSqr = Double.MAX_VALUE;

            int r = (int) Math.ceil(radius);

            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    for (int dz = -r; dz <= r; dz++) {
                        BlockPos checkPos = center.offset(dx, dy, dz);

                        // 手动计算与 eyePos 的距离平方
                        double cx = checkPos.getX() + 0.5;
                        double cy = checkPos.getY() + 0.5;
                        double cz = checkPos.getZ() + 0.5;

                        double dxSq = cx - eyePos.x;
                        double dySq = cy - eyePos.y;
                        double dzSq = cz - eyePos.z;

                        double distSq = dxSq * dxSq + dySq * dySq + dzSq * dzSq;

                        if (distSq > radius * radius) continue;

                        BlockState state = mc.level.getBlockState(checkPos);
                        if (state.is(Blocks.WATER) && state.getFluidState().isSource()) {
                            if (distSq < nearestDistanceSqr) {
                                nearestDistanceSqr = distSq;

                                nearestWater = checkPos;
//                                System.out.printf(state);
                            }
                        }
                    }
                }
            }

            if (nearestWater != null) {
                Vec3 targetCenter = new Vec3(
                        nearestWater.getX() + 0.5,
                        nearestWater.getY() + 0.5,
                        nearestWater.getZ() + 0.5
                );

                Vec3 toTarget = targetCenter.subtract(eyePos);

                double dx = toTarget.x;
                double dy = toTarget.y;
                double dz = toTarget.z;

                float yaw = (float)- Math.toDegrees(Math.atan2(toTarget.x, toTarget.z));
                float pitch = -(float) Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

//                System.out.println("NeWater" + nearestWater);
//                System.out.printf("SHIJIAO：Yaw = %.2f°, Pitch = %.2f°%n", yaw, pitch);

                return new LookAngles(yaw, pitch);
            } else {
                return new LookAngles(-1,-1);
            }
        }
        }
        // 获取下一个动作序列号，以匹配客户端多玩家模式要求
//    public static boolean isSupportedItem(ItemStack stack) {
//        return stack.getItem() == Items.WATER_BUCKET/* || stack.getItem() == Items.SLIME_BLOCK*/;
//    }

//    public static double distanceToGround(double ySpeed) {
//        Player player = Minecraft.getInstance().player;
//
//        AABB bb = player.getBoundingBox();
//
//        AABB toCheck = new AABB(bb.minX,bb.minY - (ySpeed * 20),bb.minZ,bb.maxX,bb.maxY,bb.maxZ);
//
//        List<BlockPos> list = Utils.getAllInBox(toCheck).map(BlockPos::toImmutable).collect(Collectors.toList());
//
//        for (BlockPos pos : list) {
//            BlockState state = player.world.getBlockState(pos);
//            VoxelShape shape1 = state.getCollisionShape(player.world,pos, ISelectionContext.forEntity(player));
//
//            shape1 = shape1.withOffset(pos.getX(),pos.getY(),pos.getZ());
//
//            VoxelShape playerShape = VoxelShapes.create(toCheck);
//
//            if (!VoxelShapes.combine(shape1,playerShape, IBooleanFunction.AND).isEmpty()) {
//                return player.getPosY() - pos.getY();
//            }
//        }
//        return 255;
//    }

//    public static final ClientConfig CLIENT;
//    public static final ForgeConfigSpec CLIENT_SPEC;
//
//    static {
//        final Pair<ClientConfig, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
//        CLIENT_SPEC = specPair2.getRight();
//        CLIENT = specPair2.getLeft();
//    }


//    public static class ClientConfig {
//
//        public static ForgeConfigSpec.IntValue threshold;
//
//        public ClientConfig(ForgeConfigSpec.Builder builder) {
//            builder.push("general");
//            threshold = builder.
//                    comment("This is the minimum distance required at which mlg saves will kick in")
//                    .defineInRange("threshold",12,4,2047);
//            builder.pop();
//        }
//    }
    }

