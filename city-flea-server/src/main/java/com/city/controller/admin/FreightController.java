package com.city.controller.admin;

import com.city.dto.FreightConfigDTO;
import com.city.entity.FreightConfig;
import com.city.result.Result;
import com.city.service.FreightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/freight")
@Tag(name = "运费配置管理")
@Slf4j
public class FreightController {

    @Autowired
    private FreightService freightService;

    @GetMapping("/list")
    @Operation(summary = "运费配置列表")
    public Result<List<FreightConfig>> list() {
        return Result.success(freightService.listAllConfigs());
    }

    @GetMapping("/enabled")
    @Operation(summary = "启用的运费配置列表")
    public Result<List<FreightConfig>> listEnabled() {
        return Result.success(freightService.listEnabledConfigs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取运费配置")
    public Result<FreightConfig> getById(@PathVariable Long id) {
        FreightConfig config = freightService.getConfigById(id);
        return Result.success(config);
    }

    @PostMapping
    @Operation(summary = "新增运费配置")
    public Result<String> add(@RequestBody FreightConfigDTO dto) {
        freightService.addConfig(dto);
        return Result.success("新增成功");
    }

    @PutMapping
    @Operation(summary = "修改运费配置")
    public Result<String> update(@RequestBody FreightConfigDTO dto) {
        freightService.updateConfig(dto);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除运费配置")
    public Result<String> delete(@PathVariable Long id) {
        freightService.deleteConfig(id);
        return Result.success("删除成功");
    }

    @PutMapping("/enabled/{id}")
    @Operation(summary = "启用/禁用运费配置")
    public Result<String> toggleEnabled(@PathVariable Long id, @RequestParam Integer enabled) {
        freightService.toggleConfigEnabled(id, enabled);
        return Result.success(enabled == 1 ? "启用成功" : "禁用成功");
    }
}
