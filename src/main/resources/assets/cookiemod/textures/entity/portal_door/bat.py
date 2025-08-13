from PIL import Image
import glob
import os


def copy_img(img_path, output_path):
    # 打开要复制的PNG图片
    image = Image.open(img_path)

    # 获取图片的宽度和高度
    width, height = image.size

    # 创建一个新的图片对象，宽度为原图片宽度的两倍，高度与原图片相同
    new_image = Image.new('RGBA', (width * 2, height))

    # 将原图片粘贴到新图片的指定位置
    new_image.paste(image, (0, 0))
    new_image.paste(image, (width, 0))

    # 保存新图片
    new_image.save(output_path)


def pixelate_image(image_path, output_path, pixel_size):
    # 加载图片并调整大小
    img = Image.open(image_path)
    resized_img = img.resize((img.width // pixel_size, img.height // pixel_size), Image.NEAREST)

    # 保存结果
    resized_img.save(output_path)


PATH = "./*.png"
Output_Path = "./lnew/"
for i in glob.glob(PATH):
    pixelate_image(i, os.path.join(Output_Path, i), 7)
    copy_img(os.path.join(Output_Path, i), os.path.join(Output_Path, i))
