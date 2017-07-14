package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.basic.common.enums.WorkTimeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto;
import net.myspring.basic.modules.hr.dto.DutyWorktimeExportDto;
import net.myspring.basic.modules.hr.repository.*;
import net.myspring.basic.modules.hr.web.form.DutyWorktimeForm;
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery;
import net.myspring.basic.modules.sys.client.FolderFileClient;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.time.LocalTimeUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class DutyWorktimeService {
    @Autowired
    private DutyWorktimeRepository dutyWorktimeRepository;
    @Autowired
    private DutyLeaveRepository dutyLeaveRepository;
    @Autowired
    private DutyOvertimeRepository dutyOvertimeRepository;
    @Autowired
    private DutyRestRepository dutyRestRepository;
    @Autowired
    private DutySignRepository dutySignRepository;
    @Autowired
    private DutyFreeRepository dutyFreeRepository;
    @Autowired
    private DutyPublicFreeRepository dutyPublicFreeRepository;
    @Autowired
    private DutyTripRepository dutyTripRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private FolderFileClient folderFileClient;

    public Page<DutyWorktimeDto> findPage(Pageable pageable, DutyWorktimeQuery dutyWorktimeQuery) {
        Page<DutyWorktimeDto> page = dutyWorktimeRepository.findPage(pageable, dutyWorktimeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DutyWorktimeExportDto> getDutyWorktimeExportDto(LocalDate dateStart, LocalDate dateEnd) {
        List<String> statusList = Lists.newArrayList();
        statusList.add(AuditTypeEnum.APPLY.getValue());
        statusList.add(AuditTypeEnum.PASS.getValue());
        Map<String, Map<String, String>> cellTypeMap = Maps.newHashMap();
        //获取考勤人员
        Set<String> employeeIdSet = Sets.newHashSet();
        //请假
        Map<String, List<String>> leaveMap = Maps.newHashMap();
        List<DutyLeave> dutyLeaves = dutyLeaveRepository.findByDateAndStatusList(dateStart, dateEnd, statusList);
        for (DutyLeave dutyLeave : dutyLeaves) {
            String key = dutyLeave.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyLeave.getDutyDate());
            if (!leaveMap.containsKey(key)) {
                leaveMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("leave")) {
                Map<String, String> leaveCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("leave", leaveCellTypeMap);
            }
            cellTypeMap.get("leave").put(key, ExcelCellStyle.RED.name());
            employeeIdSet.add(dutyLeave.getEmployeeId());
            leaveMap.get(key).add(dutyLeave.getDateType() + "/" + dutyLeave.getLeaveType());
        }
        //加班
        Map<String, List<String>> overtimeMap = Maps.newHashMap();
        List<DutyOvertime> dutyOvertimes = dutyOvertimeRepository.findByDateAndStatusList(dateStart, dateEnd, statusList);
        for (DutyOvertime dutyOvertime : dutyOvertimes) {
            String key = dutyOvertime.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyOvertime.getDutyDate());
            if (!overtimeMap.containsKey(key)) {
                overtimeMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("overtime")) {
                Map<String, String> overtimeCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("overtime", overtimeCellTypeMap);
            }
            if (AuditTypeEnum.APPLY.getValue().equals(dutyOvertime.getStatus())) {
                cellTypeMap.get("overtime").put(key, ExcelCellStyle.LIGHT_BLUE.name());
            }
            employeeIdSet.add(dutyOvertime.getEmployeeId());
            overtimeMap.get(key).add(LocalTimeUtils.format(dutyOvertime.getTimeStart()) + "~" + LocalTimeUtils.format(dutyOvertime.getTimeEnd()));
        }

        //加班调休
        Map<String, List<String>> overtimeRestMap = Maps.newHashMap();
        List<DutyRest> overtimeDutyRests = dutyRestRepository.findByTypeAndDutyDate(DutyRestTypeEnum.加班调休.name(), dateStart, dateEnd, statusList);
        for (DutyRest dutyRest : overtimeDutyRests) {
            String key = dutyRest.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyRest.getDutyDate());
            if (!overtimeRestMap.containsKey(key)) {
                overtimeRestMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("overtimeRest")) {
                Map<String, String> overtimeRestCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("overtimeRest", overtimeRestCellTypeMap);
            }
            if (AuditTypeEnum.APPLY.getValue().equals(dutyRest.getStatus())) {
                cellTypeMap.get("overtimeRest").put(key, ExcelCellStyle.LIGHT_BLUE.name());
            }
            employeeIdSet.add(dutyRest.getEmployeeId());
            overtimeRestMap.get(key).add(LocalTimeUtils.format(dutyRest.getTimeStart()) + "~" + LocalTimeUtils.format(dutyRest.getTimeEnd()));
        }
        //年假调休
        Map<String, List<String>> annualRestMap = Maps.newHashMap();
        List<DutyRest> annualRests = dutyRestRepository.findByTypeAndDutyDate(DutyRestTypeEnum.年假调休.name(), dateStart, dateEnd, statusList);
        for (DutyRest dutyRest : annualRests) {
            String key = dutyRest.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyRest.getDutyDate());
            if (!annualRestMap.containsKey(key)) {
                annualRestMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("annualRest")) {
                Map<String, String> annualRestCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("annualRest", annualRestCellTypeMap);
            }
            if (AuditTypeEnum.APPLY.getValue().equals(dutyRest.getStatus())) {
                cellTypeMap.get("annualRest").put(key, ExcelCellStyle.LIGHT_BLUE.name());
            }
            employeeIdSet.add(dutyRest.getEmployeeId());
            annualRestMap.get(key).add(dutyRest.getDateType());
        }
        //免打卡
        Map<String, List<String>> freeMap = Maps.newHashMap();
        List<DutyFree> dutyFrees = dutyFreeRepository.findByDateAndStatusList(dateStart, dateEnd, statusList);
        for (DutyFree dutyFree : dutyFrees) {
            String key = dutyFree.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyFree.getFreeDate());
            if (!freeMap.containsKey(key)) {
                freeMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("free")) {
                Map<String, String> freeCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("free", freeCellTypeMap);
            }
            if (AuditTypeEnum.APPLY.getValue().equals(dutyFree.getStatus())) {
                cellTypeMap.get("free").put(key, ExcelCellStyle.LIGHT_BLUE.name());
            }
            employeeIdSet.add(dutyFree.getEmployeeId());
            if (!freeMap.get(key).contains(dutyFree.getDateType())) {
                freeMap.get(key).add(dutyFree.getDateType());
            }
        }
        //公休
        Map<String, List<String>> publicFreeMap = Maps.newHashMap();
        List<DutyPublicFree> dutyPublicFrees = dutyPublicFreeRepository.findByDateAndStatusList(dateStart, dateEnd, statusList);
        for (DutyPublicFree dutyPublicFree : dutyPublicFrees) {
            String key = dutyPublicFree.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyPublicFree.getFreeDate());
            if (!publicFreeMap.containsKey(key)) {
                publicFreeMap.put(key, new ArrayList<String>());
            }
            if (!cellTypeMap.containsKey("publicFree")) {
                Map<String, String> publicFreeCellTypeMap = Maps.newHashMap();
                cellTypeMap.put("publicFree", publicFreeCellTypeMap);
            }
            if (AuditTypeEnum.APPLY.getValue().equals(dutyPublicFree.getStatus())) {
                cellTypeMap.get("publicFree").put(key, ExcelCellStyle.LIGHT_BLUE.name());
            }
            employeeIdSet.add(dutyPublicFree.getEmployeeId());
            if (!publicFreeMap.get(key).contains(dutyPublicFree.getDateType())) {
                publicFreeMap.get(key).add(dutyPublicFree.getDateType());
            }
        }


        //出差
        Map<String, List<String>> tripMap = Maps.newHashMap();
        List<DutyTrip> dutyTrips = dutyTripRepository.findByDateAndStatusList(dateStart, dateEnd, statusList);
        for (DutyTrip dutyTrip : dutyTrips) {
            List<LocalDate> dateList = LocalDateUtils.getDateList(dutyTrip.getDateStart(), dutyTrip.getDateEnd());
            for (LocalDate date : dateList) {
                String key = dutyTrip.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(date);
                if (!tripMap.containsKey(key)) {
                    tripMap.put(key, new ArrayList<String>());
                }
                if (!tripMap.get(key).contains("出差")) {
                    tripMap.get(key).add("出差");
                }
                if (!cellTypeMap.containsKey("trip")) {
                    Map<String, String> tripCellTypeMap = Maps.newHashMap();
                    cellTypeMap.put("trip", tripCellTypeMap);
                }
                if (AuditTypeEnum.APPLY.getValue().equals(dutyTrip.getStatus())) {
                    cellTypeMap.get("trip").put(key, ExcelCellStyle.LIGHT_BLUE.name());
                }
            }
            employeeIdSet.add(dutyTrip.getEmployeeId());
        }

        //打卡记录
        Map<String, List<String>> workTimeMap = Maps.newHashMap();
        Map<String, String> workTimeStartMap = Maps.newHashMap();
        Map<String, String> workTimeEndMap = Maps.newHashMap();
        Map<String, String> workTimeFirstMap = Maps.newHashMap();
        Map<String, String> workTimeLastMap = Maps.newHashMap();
        Map<String, List<String>> workTimeAm = Maps.newHashMap();
        Map<String, List<String>> workTimePm = Maps.newHashMap();
        List<DutyWorktime> dutyWorktimes = dutyWorktimeRepository.findByDutyDate(dateStart, dateEnd);
        for (DutyWorktime dutyWorktime : dutyWorktimes) {
            String key = dutyWorktime.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyWorktime.getDutyDate());
            String value = dutyWorktime.getType() + ":" + LocalTimeUtils.format(dutyWorktime.getDutyTime());
            if (!workTimeMap.containsKey(key)) {
                workTimeMap.put(key, new ArrayList<String>());
            }
            employeeIdSet.add(dutyWorktime.getEmployeeId());
            workTimeMap.get(key).add(value);
            if (dutyWorktime.getDutyTime() != null && LocalTimeUtils.isAm(dutyWorktime.getDutyTime())) {
                workTimeStartMap.put(key, value);
                if (!workTimeAm.containsKey(key)) {
                    workTimeAm.put(key, new ArrayList<String>());
                }
                workTimeAm.get(key).add(value);
            } else {
                workTimeEndMap.put(key, value);
                if (!workTimePm.containsKey(key)) {
                    workTimePm.put(key, new ArrayList<String>());
                }
                workTimePm.get(key).add(value);
            }
        }
        for (String key : workTimeAm.keySet()) {
            String first = workTimeAm.get(key).get(0);
            String last = "";
            int size = workTimeAm.get(key).size();
            if (size > 1) {
                last = workTimeAm.get(key).get(size - 1);
            }
            String value = first + "~" + last;
            workTimeFirstMap.put(key, value);
        }

        for (String key : workTimePm.keySet()) {
            String first = workTimePm.get(key).get(0);
            String last = "";
            int size = workTimePm.get(key).size();
            if (size > 1) {
                last = workTimePm.get(key).get(size - 1);
            }
            String value = first + "~" + last;
            workTimeLastMap.put(key, value);
        }


        Map<String, DutyWorktimeExportDto> dutyWorktimeMap = Maps.newTreeMap();
        Map<String, Employee> employeeMap = employeeRepository.findMap(employeeIdSet);
        Map<String, Account> accountMap = accountRepository.findMap(CollectionUtil.extractToList(employeeMap.values(), "accountId"));
        Map<String, Office> officeMap = officeRepository.findMap(CollectionUtil.extractToList(accountMap.values(), "officeId"));
        for (String employeeId : employeeIdSet) {
            for (LocalDate date : LocalDateUtils.getDateList(dateStart, dateEnd)) {
                String key = employeeId + CharConstant.ENTER + LocalDateUtils.format(date);
                Employee employee = employeeMap.get(employeeId);
                Account account = accountMap.get(employee.getAccountId());
                Office office = officeMap.get(account.getOfficeId());
                DutyWorktimeExportDto dutyWorktime = new DutyWorktimeExportDto();
                dutyWorktime.setEmployeeId(employeeId);
                dutyWorktime.setEmployeeName(employee.getName());
                dutyWorktime.setOfficeName(office.getName());
                dutyWorktime.setDutyDate(date);
                dutyWorktime.setWorktimes(workTimeMap.get(key));
                dutyWorktime.setLeaves(leaveMap.get(key));
                dutyWorktime.setOvertimes(overtimeMap.get(key));
                dutyWorktime.setAnnualRests(annualRestMap.get(key));
                dutyWorktime.setFrees(freeMap.get(key));
                dutyWorktime.setPublicFrees(publicFreeMap.get(key));
                dutyWorktime.setTrips(tripMap.get(key));
                dutyWorktime.setOvertimeRests(overtimeRestMap.get(key));
                dutyWorktime.setStartTime(workTimeStartMap.get(key));
                dutyWorktime.setEndTime(workTimeEndMap.get(key));
                dutyWorktime.setFirstTime(workTimeFirstMap.get(key));
                dutyWorktime.setLastTime(workTimeLastMap.get(key));
                dutyWorktime.setStyle(getCellStyle(dutyWorktime));
                String overtimeCellType = cellTypeMap.get("overtime") != null && cellTypeMap.get("overtime").get(key) != null ? cellTypeMap.get("overtime").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setOvertimeCellType(overtimeCellType);
                String annualRestCellType = cellTypeMap.get("annualRest") != null && cellTypeMap.get("annualRest").get(key) != null ? cellTypeMap.get("annualRest").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setAnnualRestCellType(annualRestCellType);
                String freeCellType = cellTypeMap.get("free") != null && cellTypeMap.get("free").get(key) != null ? cellTypeMap.get("free").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setFreeCellType(freeCellType);
                String publicFreeCellType = cellTypeMap.get("publicFree") != null && cellTypeMap.get("publicFree").get(key) != null ? cellTypeMap.get("publicFree").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setPublicFreeCellType(publicFreeCellType);
                String tripCellType = cellTypeMap.get("trip") != null && cellTypeMap.get("trip").get(key) != null ? cellTypeMap.get("trip").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setTripCellType(tripCellType);
                String overtimeRestCellType = cellTypeMap.get("overtimeRest") != null && cellTypeMap.get("overtimeRest").get(key) != null ? cellTypeMap.get("overtimeRest").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setOvertimeRestCellType(overtimeRestCellType);
                String leaveCellType = cellTypeMap.get("leave") != null && cellTypeMap.get("leave").get(key) != null ? cellTypeMap.get("leave").get(key) : dutyWorktime.getStyle();
                dutyWorktime.setLeaveCellType(leaveCellType);
                dutyWorktimeMap.put(key, dutyWorktime);
            }
        }
        return Lists.newArrayList(dutyWorktimeMap.values());
    }

    @Transactional
    public void save(String folderFileId, String yearMonth, String remarks) {
        Map<String, DutyWorktime> dutyWorktimeMap = Maps.newLinkedHashMap();
        FolderFileFeignDto folderFile = folderFileClient.findById(folderFileId);
        Workbook workbook = ExcelUtils.getWorkbook(new File(folderFile.getUploadPath(RequestUtils.getCompanyName())));
        Sheet sheetAt = workbook.getSheetAt(0);
        int rowCount = sheetAt.getLastRowNum();
        if (rowCount > 1) {
            String dateRegex = "^\\d\\d.\\d\\d .*$";
            List<String> loginNameList = Lists.newArrayList();
            for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
                String loginName = StringUtils.toString(ExcelUtils.getCellValue(sheetAt.getRow(i).getCell(14)));
                if (StringUtils.isNotBlank(loginName)) {
                    loginNameList.add(loginName);
                }
            }
            String employeeId = null;
            List<Account> accountList = accountRepository.findByAccessLoginNameList(loginNameList, LocalDate.now());
            Map<String, Account> accountMap = CollectionUtil.extractToMap(accountList, "loginName");
            for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
                Row row = sheetAt.getRow(i);
                if (row.getLastCellNum() > 14) {
                    String loginName = StringUtils.toString(ExcelUtils.getCellValue(row.getCell(14)));
                    if (StringUtils.isNotBlank(loginName)) {
                        Account account = accountMap.get(loginName);
                        if (account != null) {
                            employeeId = account.getEmployeeId();
                        }
                    }
                }
                boolean isDateRow = false;
                String tempDate = "";
                if (row.getLastCellNum() > 14) {
                    tempDate = String.valueOf(row.getCell(2)).trim();
                    if (Pattern.matches(dateRegex, tempDate)) {
                        isDateRow = true;
                    }
                }
                if (StringUtils.isNotBlank(employeeId) && isDateRow) {
                    for (int j = 1; j < row.getLastCellNum(); j++) {
                        tempDate = (String) ExcelUtils.getCellValue(row.getCell(j));
                        if (StringUtils.isNotBlank(tempDate)) {
                            Object worktime = ExcelUtils.getCellValue(sheetAt.getRow(i + 1).getCell(j));
                            String dateStr = yearMonth.split("-")[0] + "-" + tempDate.split(" ")[0].replace("/", "-");
                            LocalDate dutyDate = LocalDateUtils.parse(dateStr);
                            LocalTime startTime = null;
                            LocalTime endTime = null;
                            if (StringUtils.isNotBlank(String.valueOf(worktime))) {
                                if (worktime instanceof Date) {
                                    Date date = (Date)worktime;
                                    Instant instant = date.toInstant();
                                    ZoneId zone = ZoneId.systemDefault();
                                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
                                    startTime = localDateTime.toLocalTime();
                                } else {
                                    worktime = String.valueOf(worktime).trim().replace("：", ":");
                                    if (!"-".equals(worktime)) {
                                        String[] worktimes = String.valueOf(worktime).split("-");
                                        if (worktimes[0] != null && StringUtils.isNotBlank(worktimes[0])) {
                                            startTime = LocalTimeUtils.parse(worktimes[0] + ":00");
                                        }
                                        if (worktimes.length > 1 && worktimes[worktimes.length - 1] != null && StringUtils.isNotBlank(worktimes[worktimes.length - 1])) {
                                            endTime = LocalTimeUtils.parse(worktimes[worktimes.length - 1] + ":00");
                                        }
                                    }
                                }
                            }
                            // 插入数据
                            if (startTime != null) {
                                DutyWorktime dutyWorktime = new DutyWorktime();
                                String key = employeeId + CharConstant.ENTER + LocalDateUtils.format(dutyDate) + CharConstant.ENTER + LocalTimeUtils.format(startTime);
                                dutyWorktime.setEmployeeId(employeeId);
                                dutyWorktime.setDutyDate(dutyDate);
                                dutyWorktime.setDutyTime(startTime);
                                dutyWorktime.setType(WorkTimeTypeEnum.打卡.toString());
                                dutyWorktimeMap.put(key, dutyWorktime);
                            }
                            if (endTime != null) {
                                DutyWorktime dutyWorktime = new DutyWorktime();
                                String key = employeeId + CharConstant.ENTER + LocalDateUtils.format(dutyDate) + CharConstant.ENTER + LocalTimeUtils.format(endTime);
                                dutyWorktime.setEmployeeId(employeeId);
                                dutyWorktime.setDutyDate(dutyDate);
                                dutyWorktime.setDutyTime(endTime);
                                dutyWorktime.setType(WorkTimeTypeEnum.打卡.toString());
                                dutyWorktimeMap.put(key, dutyWorktime);
                            }
                        }
                    }
                    i = i + 1;
                }
            }
            Collection<DutyWorktime> dutyWorktimes = dutyWorktimeMap.values();
            if (CollectionUtil.isNotEmpty(dutyWorktimes)) {
                Set<String> deleted = Sets.newHashSet();
                for (DutyWorktime dutyWorktime : dutyWorktimes) {
                    String key = dutyWorktime.getEmployeeId() + CharConstant.ENTER + LocalDateUtils.format(dutyWorktime.getDutyDate());
                    if (!deleted.contains(key)) {
                        dutyWorktimeRepository.deleteByEmployeeAndDutyDateAndType(dutyWorktime.getEmployeeId(), dutyWorktime.getDutyDate(), WorkTimeTypeEnum.打卡.toString());
                        deleted.add(key);
                    }
                }
                dutyWorktimeRepository.save(dutyWorktimes);
            }
        }
    }

    public String findSimpleExcelSheet(String month, Workbook workbook, List<DutyWorktimeExportDto> dutyWorktimeExporeList) {
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "部门"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "打卡日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "星期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "打卡时间（上午）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "打卡时间（下午）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "加班"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "请假"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "加班调休"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "年假调休"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "免打卡"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "公休"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "出差"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "打卡时间（初次）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "打卡时间（末次）"));
        excelColumnList.add(simpleExcelColumnList);
        for (DutyWorktimeExportDto dutyWorktimeExportDto : dutyWorktimeExporeList) {
            simpleExcelColumnList = Lists.newArrayList();
            CellStyle cellStyle = cellStyleMap.get(dutyWorktimeExportDto.getStyle());
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getOfficeName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getEmployeeName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, LocalDateUtils.format(dutyWorktimeExportDto.getDutyDate())));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getWeekOfDay()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getStartTime()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getEndTime()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getOvertimeCellType()), StringUtils.join(dutyWorktimeExportDto.getOvertimes(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getLeaveCellType()), StringUtils.join(dutyWorktimeExportDto.getLeaves(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getOvertimeRestCellType()), StringUtils.join(dutyWorktimeExportDto.getOvertimeRests(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getAnnualRestCellType()), StringUtils.join(dutyWorktimeExportDto.getAnnualRests(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getFreeCellType()), StringUtils.join(dutyWorktimeExportDto.getFrees(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getPublicFreeCellType()), StringUtils.join(dutyWorktimeExportDto.getPublicFrees(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(dutyWorktimeExportDto.getTripCellType()), StringUtils.join(dutyWorktimeExportDto.getTrips(), CharConstant.ENTER)));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getFirstTime()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyle, dutyWorktimeExportDto.getLastTime()));
            excelColumnList.add(simpleExcelColumnList);
        }
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("考勤记录", excelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook, "考勤记录" + month + ".xlsx", simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream = ExcelUtils.doWrite(simpleExcelBook.getWorkbook(), simpleExcelBook.getSimpleExcelSheets());
        return null;
    }

    private String getCellStyle(DutyWorktimeExportDto dutyWorktime) {
        String cellStyle = ExcelCellStyle.DATA.name();
        boolean success = false;
        if (dutyWorktime.getStartTime() != null && dutyWorktime.getEndTime() != null && CollectionUtil.isEmpty(dutyWorktime.getOvertimeRests())) {
            success = true;
        }
        if (dutyWorktime.getStartTime() == null && dutyWorktime.getEndTime() != null && CollectionUtil.isNotEmpty(dutyWorktime.getAnnualRests())) {
            if (dutyWorktime.getAnnualRests().contains(DutyDateTypeEnum.上午.name())) {
                success = true;
            }
        }
        if (dutyWorktime.getStartTime() != null && dutyWorktime.getEndTime() == null && CollectionUtil.isNotEmpty(dutyWorktime.getAnnualRests())) {
            if (dutyWorktime.getAnnualRests().contains(DutyDateTypeEnum.下午.name())) {
                success = true;
            }
        }
        if (dutyWorktime.getStartTime() == null && dutyWorktime.getEndTime() == null && CollectionUtil.isNotEmpty(dutyWorktime.getAnnualRests())) {
            if (dutyWorktime.getAnnualRests().contains(DutyDateTypeEnum.上午.name()) && dutyWorktime.getAnnualRests().contains(DutyDateTypeEnum.下午.name())
                    || dutyWorktime.getAnnualRests().contains(DutyDateTypeEnum.全天.name())) {
                success = true;
            }
        }
        if (dutyWorktime.getStartTime() != null && CollectionUtil.isNotEmpty(dutyWorktime.getFrees()) && dutyWorktime.getFrees().contains(DutyDateTypeEnum.下午.name())) {
            success = true;
        }
        if (dutyWorktime.getEndTime() != null && CollectionUtil.isNotEmpty(dutyWorktime.getFrees()) && dutyWorktime.getFrees().contains(DutyDateTypeEnum.上午.name())) {
            success = true;
        }
        if (CollectionUtil.isNotEmpty(dutyWorktime.getFrees()) && ((dutyWorktime.getFrees().contains(DutyDateTypeEnum.上午.name()) && dutyWorktime.getFrees().contains(DutyDateTypeEnum.下午.name()))
                || (dutyWorktime.getFrees().contains(DutyDateTypeEnum.全天.name())))) {
            success = true;
        }
        if (CollectionUtil.isNotEmpty(dutyWorktime.getTrips()) && dutyWorktime.getTrips().contains("出差")) {
            success = true;
        }
        if (success) {
            cellStyle = ExcelCellStyle.LIGHT_GREEN.name();
        }
        return cellStyle;
    }
}
