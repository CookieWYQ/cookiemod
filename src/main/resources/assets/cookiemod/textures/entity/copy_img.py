from PIL import Image

# 打开要复制的PNG图片
image = Image.open("./test.png")

# 获取图片的宽度和高度
width, height = image.size

# 创建一个新的图片对象，宽度为原图片宽度的两倍，高度与原图片相同
new_image = Image.new('RGBA', (width * 2, height))

# 将原图片粘贴到新图片的指定位置
new_image.paste(image, (0, 0))
new_image.paste(image, (width, 0))

# 保存新图片
new_image.save("./test.png")
