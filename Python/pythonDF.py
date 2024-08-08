import pandas as pd

# Sample DataFrame
data = {
    'name': ['Andre', 'Ramin', 'Priyanshu'],
    'place': ['Kiev', 'Tehran', 'Delhi'],
    'countrycode': ['UA', 'IR', 'IN']
}

df = pd.DataFrame(data)

# Sample dictionary with country codes and country names
country_dict = {
    'UA': 'Ukrain',
    'IR': 'Iran',
    'IN': 'India'
}

# Map countrycode to countryname
df['countryname'] = df['countrycode'].map(country_dict)

# Output the final DataFrame
print(df)
