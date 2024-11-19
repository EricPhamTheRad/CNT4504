import com.pi4j.context.Context;
import com.pi4j.io.exception.IOException;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiBus;
import com.pi4j.io.spi.SpiChipSelect;
import com.pi4j.io.spi.SpiMode;
/**
    Handles Serial Peripheral Interface communication with the LIS3DH

    SPI starts by enabling a chip by pulling a pin low
    the Binary value of the register address is transmitted bitwise on every clock edge
    if a read bit is sent then the LIS3DH sends the data bit wise
    This is handled by the spi.transfer() method

 */
public class LIS3DH {

    // LIS3DH register addresses and constants
    private static final byte CTRL_REG1 = (byte) 0x20; // Control Register 1
    private static final byte OUT_X_L = (byte) 0x28;  // Output X low register
    private static final byte OUT_X_H = (byte) 0x29;  // Output X high register
    private static final byte OUT_Y_L = (byte) 0x2A;  // Output Y low register
    private static final byte OUT_Y_H = (byte) 0x2B;  // Output Y high register
    private static final byte OUT_Z_L = (byte) 0x2C;  // Output Z low register
    private static final byte OUT_Z_H = (byte) 0x2D;  // Output Z high register

    /**
     * Constructor for the LIS3DH object to use SPI
     * @param pi4j object contains information about the Raspberry Pi board need for SPI
     * @param spiBus object used for the sending and receiving of data
     * @param chipSelect chooses which pin of the Raspberry Pi used to enable the LIS3DH

    */
    public LIS3DH(Context pi4j, SpiBus spiBus, SpiChipSelect chipSelect) {
        this.pi4j = pi4j;
        this.chipSelect = chipSelect;
        this.spiBus = spiBus;

        init();
    }

    /**
     * Initialises the object with configuration
     *
     */
    private void init() {
        var spiConfig = Spi.newConfigBuilder(pi4j)
                .id("SPI" + spiBus + " " + chipSelect) //ID of the object
                .name("LIS3DH Accelerometer") //Name of object
                .bus(SpiBus.BUS_0) //Data buss
                .chipSelect(chipSelect) //Which pin is used for enabling
                .baud(500000) //Speed of clock signal in Hertz
                .mode(SpiMode.MODE_0) //Determines if data is transfer on rising or falling edge
                .provider("pigpio-spi") //Which library to use for controlling the GPIO pins
                .build();
        this.spi = this.pi4j.create(spiConfig);

        // Initialize the LIS3DH by setting control registers (e.g., enable 3D mode, set output data rate)
        initializeLIS3DH();
    }

    /**
     * Configures the registers of the LIS3DH
     * Control Register 1 contains 7 bits, each bit controls a function of the LIS3DH
     * 0x57 is equivalent to 1 1 1 0 0 1 0 0
     * The first three bits enable the X, Y, Z axis of the LIS3DH
     * The fourth bit enables low power mode, this is disabled
     * The last bits control the power mode and the output data rate, normal and 50Hz were selected
     * The bits are in reverse order from documentation as the SPI enters
     * the bits The Least Significant bit to Most Significant bit order
     */
    private void initializeLIS3DH() {
        this.spi.write(CTRL_REG1, (byte) 0x57);
        System.out.println("LIS3DH initialized.");
        //who();
    }

    /**
     * Calls readAxisData method for each axis and prints it
     * @throws IOException Throws Error if no IO interface can be found
     */
    public float[] readAccelerometerData() throws IOException {
        //System.out.println(">>> Enter readAccelerometerData");
        float[] values = new float[3];

        // Read accelerometer data from LIS3DH
        System.out.println("Reading X");
        short x = readAxisData(OUT_X_L, OUT_X_H);
        values [0] =  (float) x / 16384;
        System.out.println("Reading Y");
        short y = readAxisData(OUT_Y_L, OUT_Y_H);
        values [1] =  (float) y / 16384;
        System.out.println("Reading Z");
        short z = readAxisData(OUT_Z_L, OUT_Z_H);
        values [2] =  (float) z / 16384;
        System.out.printf("X: %.2f, Y: %.2f, Z: %.2f%n",values[0],values[1],values[2]);
        //System.out.println("<<< Exit readAccelerometerData");
        return values;
    }
    /**
     * The data for acceleration is in separate register so both will have to be accessed
     * The Data is concatenated and bit shifted together to form a 16-bit integer
     */

    private short readAxisData(byte lowRegister, byte highRegister) throws IOException {
        //The register address is a 7-bit number, the 7th bit (the first bit is index as 0) is set to 1
        //to enable reading, a dummy byte is set to allow for data is set
        byte[] lowAddress = new byte[]{ (byte) (lowRegister | 0x80), (byte)0x00 };
        byte[] highAddress = new byte[]{ (byte) (highRegister | 0x80), (byte)0x00 };
        //Stores values for read data
        byte[] lowResult = new byte[2];
        byte[] highResult = new byte[2];

        //Read the low and high bytes for the given axis
        this.spi.transfer(lowAddress, lowResult);  // Gets low byte
        this.spi.transfer(highAddress, highResult);  // Gets high byte

        //Combine the bytes into a short value
        //highResult contains the 8 highest bits of the 16-bit integer and must be shifted up
        //Ex  0000 0000 1111 1111 -> 1111 1111 0000 0000
        //lowResult contains the lowest bits and is concatenated to the end
        //Ex 1111 1111 0000 0000 + 0000 0000 0101 0101 -> 1111 1111 0101 0101
        // the & 0xFF is a bit mask, to remove the starting 1s
        //Ie the results may come in as 1111 1111 1010 1010, the 8 bits in the beginning are not data ,
        return (short) ((highResult[1] & 0xFF) << 8 | (lowResult[1] & 0xFF));
    }
    public boolean isDataReady() {
        byte[] statusRegister = new byte[]{(byte) ( 0x27 | 0x80), 0x00}; // 0x80 for read operation
        byte[] response = new byte[1];
        this.spi.transfer(statusRegister, response); // Perform the SPI transfer
        System.out.println("Status: "+Integer.toBinaryString(response[0]));
        return (response[0] & 0x08) != 0;  // Check if data-ready bit (bit 3) is set
    }

    public void who() {
        byte[] sendBuffer = new byte[] { (byte) (0x0F |  0x80), (byte) 0x00 };
        byte[] receiveBuffer = new byte[1];
        spi.transfer(sendBuffer, receiveBuffer);
        byte whoAmI = receiveBuffer[0];
        System.out.println("Who Am I Response: " + (whoAmI & 0xFF));

    }

    private Spi spi;
    private final SpiChipSelect chipSelect;
    private final SpiBus spiBus;
    private final Context pi4j;
}

