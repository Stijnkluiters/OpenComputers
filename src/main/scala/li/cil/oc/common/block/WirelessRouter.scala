package li.cil.oc.common.block

import cpw.mods.fml.common.Optional
import java.util
import li.cil.oc.common.tileentity
import li.cil.oc.Settings
import li.cil.oc.util.Tooltip
import net.minecraft.client.renderer.texture.IIconRegister
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.common.util.Constants.NBT

class WirelessRouter(val parent: SimpleDelegator) extends SimpleDelegate {
  val unlocalizedName = "WirelessRouter"

  private val icons = Array.fill[IIcon](6)(null)

  // ----------------------------------------------------------------------- //

  override def tooltipLines(stack: ItemStack, player: EntityPlayer, tooltip: util.List[String], advanced: Boolean) {
    tooltip.addAll(Tooltip.get(unlocalizedName))
  }

  @Optional.Method(modid = "Waila")
  override def wailaBody(stack: ItemStack, tooltip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) {
    val nbt = accessor.getNBTData
    val node = nbt.getTagList(Settings.namespace + "componentNodes", NBT.TAG_COMPOUND).getCompoundTagAt(accessor.getSide.ordinal)
    if (node.hasKey("address")) {
      tooltip.add(StatCollector.translateToLocalFormatted(
        Settings.namespace + "gui.Analyzer.Address", node.getString("address")))
    }
    if (nbt.hasKey(Settings.namespace + "strength")) {
      tooltip.add(StatCollector.translateToLocalFormatted(
        Settings.namespace + "gui.Analyzer.WirelessStrength", nbt.getDouble(Settings.namespace + "strength").toInt.toString))
    }
  }

  override def icon(side: ForgeDirection) = Some(icons(side.ordinal))

  override def registerIcons(iconRegister: IIconRegister) = {
    icons(ForgeDirection.DOWN.ordinal) = iconRegister.registerIcon(Settings.resourceDomain + ":generic_top")
    icons(ForgeDirection.UP.ordinal) = iconRegister.registerIcon(Settings.resourceDomain + ":router_wireless_top")

    icons(ForgeDirection.NORTH.ordinal) = iconRegister.registerIcon(Settings.resourceDomain + ":router_side")
    icons(ForgeDirection.SOUTH.ordinal) = icons(ForgeDirection.NORTH.ordinal)
    icons(ForgeDirection.WEST.ordinal) = icons(ForgeDirection.NORTH.ordinal)
    icons(ForgeDirection.EAST.ordinal) = icons(ForgeDirection.NORTH.ordinal)
  }

  // ----------------------------------------------------------------------- //

  override def hasTileEntity = true

  override def createTileEntity(world: World) = Some(new tileentity.WirelessRouter)
}
