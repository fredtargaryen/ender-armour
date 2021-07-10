package com.fredtargaryen.enderarmor.loot_tables;

import com.fredtargaryen.enderarmor.EnderArmourBase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnderArmourLootModifier extends LootModifier {
    private HashMap<String, Serializer.Table> tables;

    public EnderArmourLootModifier(ILootCondition[] conditionsIn, HashMap<String, Serializer.Table> tables) {
        super(conditionsIn);
        this.tables = tables;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Serializer.Table t = this.tables.get(context.getQueriedLootTableId().toString());
        if(t != null) {
            Random random = context.getRandom();
            this.attemptToAdd(EnderArmourBase.HELMET, generatedLoot, random, t);
            this.attemptToAdd(EnderArmourBase.CHESTPLATE, generatedLoot, random, t);
            this.attemptToAdd(EnderArmourBase.LEGGINGS, generatedLoot, random, t);
            this.attemptToAdd(EnderArmourBase.BOOTS, generatedLoot, random, t);
        }
        return generatedLoot;
    }

    private void attemptToAdd(Item item, List<ItemStack> generatedLoot, Random random, Serializer.Table table) {
        if(random.nextFloat() < table.perItemSpawnProbability) {
            ItemStack newStack = item.getDefaultInstance();
            if (random.nextFloat() < table.perItemEnchantProbability) {
                newStack = EnchantmentHelper.enchantItem(random, newStack, random.nextInt(6), true);
            }
            generatedLoot.add(newStack);
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<EnderArmourLootModifier> {

        @Override
        public EnderArmourLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            HashMap<String, Table> tables = new HashMap<>();
            Iterator<JsonElement> i = object.getAsJsonArray("tables").iterator();
            while(i.hasNext()) {
                Table t = new Table(i.next().getAsJsonObject());
                tables.put(t.tableName, t);
            }
            return new EnderArmourLootModifier(conditionsIn, tables);
        }

        @Override
        public JsonObject write(EnderArmourLootModifier instance) {
            JsonObject jo = new JsonObject();
            JsonArray tables = new JsonArray();
            instance.tables.forEach((name, table) -> {
                JsonObject tableJson = new JsonObject();
                tableJson.addProperty("table_name", name);
                tableJson.addProperty("spawn_probability", table.perItemSpawnProbability);
                tableJson.addProperty("enchant_probability", table.perItemEnchantProbability);
                tables.add(tableJson);
            });
            jo.add("tables", tables);
            return jo;
        }

        public class Table
        {
            public String tableName;
            public float perItemSpawnProbability;
            public float perItemEnchantProbability;

            public Table(JsonObject jo)
            {
                this.tableName = JSONUtils.getAsString(jo, "table_name");
                this.perItemSpawnProbability = JSONUtils.getAsFloat(jo, "spawn_probability");
                this.perItemEnchantProbability = JSONUtils.getAsFloat(jo, "enchant_probability");
            }
        }
    }
}