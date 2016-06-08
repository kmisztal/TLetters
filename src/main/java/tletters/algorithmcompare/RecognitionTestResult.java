package tletters.algorithmcompare;

class RecognitionTestResult {
    private Long time;
    private Float successRate;

    public RecognitionTestResult(Long time, Float successRate) {
        this.time = time;
        this.successRate = successRate;
    }

    public Long getTime() {
        return time;
    }

    public Float getSuccessRate() {
        return successRate;
    }
}
